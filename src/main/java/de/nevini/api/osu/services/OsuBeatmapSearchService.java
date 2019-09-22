package de.nevini.api.osu.services;

import de.nevini.api.osu.external.requests.OsuApiGetBeatmapsRequest;
import de.nevini.api.osu.jpa.beatmap.OsuBeatmapViewData;
import de.nevini.api.osu.jpa.beatmap.OsuBeatmapViewRepository;
import de.nevini.api.osu.mappers.OsuBeatmapMapper;
import de.nevini.api.osu.model.*;
import de.nevini.util.Finder;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class OsuBeatmapSearchService {

    private static final Pattern PATTERN_CACHED_PART = Pattern.compile("(?i)(b|s|mapper|hash)==?(\\S+)");
    private static final Pattern PATTERN_QUERY_PART = Pattern.compile("(?i)(?:([a-z]+)([=<>]=?|!=|<>))?(\\S+)");
    private static final Pattern PATTERN_SORT_PART = Pattern.compile("(?i)(sort)([=<>]=?|!=|<>)(\\S+)");

    private final OsuBeatmapViewRepository repository;
    private final OsuBeatmapService beatmapService;

    public OsuBeatmapSearchService(
            @Autowired OsuBeatmapViewRepository repository,
            @Autowired OsuBeatmapService beatmapService
    ) {
        this.repository = repository;
        this.beatmapService = beatmapService;
    }

    public List<OsuBeatmap> search(@NonNull String query) {
        // make sure data referenced by id is available
        ensureCached(query);
        // return 10 newest results of the free form query
        return repository.findAll((root, ignore, builder) -> buildQueryPredicate(root, query.trim(), builder),
                PageRequest.of(0, 10, Sort.by(getSortOrder(query), Sort.Order.desc("approvedDate"),
                        Sort.Order.desc("lastUpdate"), Sort.Order.asc("mode"), Sort.Order.asc("difficultyRating")))
        ).stream().map(OsuBeatmapMapper::map).collect(Collectors.toList());
    }

    private void ensureCached(String query) {
        Matcher matcher = PATTERN_CACHED_PART.matcher(query);
        while (matcher.find()) {
            if ("b".equals(matcher.group(1))) {
                ensureCachedBeatmapId(matcher.group(2));
            } else if ("s".equals(matcher.group(1))) {
                ensureCachedBeatmapsetId(matcher.group(2));
            } else if ("mapper".equals(matcher.group(1))) {
                ensureCachedCreator(matcher.group(2));
            } else if ("hash".equals(matcher.group(1))) {
                ensureCachedHash(matcher.group(2));
            }
        }
    }

    private void ensureCachedBeatmapId(String value) {
        try {
            beatmapService.getCached(Integer.parseInt(value));
        } catch (NumberFormatException ignore) {
        }
    }

    private void ensureCachedBeatmapsetId(String value) {
        try {
            beatmapService.get(OsuApiGetBeatmapsRequest.builder().beatmapsetId(Integer.parseInt(value)).build());
        } catch (NumberFormatException ignore) {
        }
    }

    private void ensureCachedCreator(String value) {
        beatmapService.get(OsuApiGetBeatmapsRequest.builder().user(value).limit(10).build());
    }

    private void ensureCachedHash(String value) {
        beatmapService.get(OsuApiGetBeatmapsRequest.builder().beatmapHash(value).build());
    }

    private Predicate buildQueryPredicate(Root<OsuBeatmapViewData> root, String query, CriteriaBuilder builder) {
        Collection<Predicate> queryPartPredicates = new ArrayList<>();
        for (String queryPart : query.split("\\s+")) {
            // extract information from query part
            Matcher matcher = PATTERN_QUERY_PART.matcher(queryPart);
            if (!matcher.matches()) continue;
            String attribute = StringUtils.defaultIfEmpty(matcher.group(1), "text");
            String comparator = StringUtils.defaultIfEmpty(matcher.group(2), "=");
            String value = matcher.group(3);
            if (StringUtils.isEmpty(value)) continue;
            // add predicate if the input can be resolved
            Predicate predicate = buildQueryPartPredicate(root, attribute, comparator, value, builder);
            if (predicate == null) continue;
            queryPartPredicates.add(predicate);
        }
        return builder.and(queryPartPredicates.toArray(new Predicate[0]));
    }

    private Predicate buildQueryPartPredicate(
            Root<OsuBeatmapViewData> root, String attribute, String comparator, String value, CriteriaBuilder builder
    ) {
        String column = getColumnName(attribute);
        switch (attribute) {
            case "b":
            case "s":
            case "length":
            case "drain":
            case "combo":
            case "circles":
            case "sliders":
            case "spinners":
            case "favs":
            case "plays":
            case "pass":
                return buildQueryPartIntegerPredicate(root.get(column), comparator, value, builder);
            case "artist":
            case "title":
            case "diff":
            case "source":
            case "tag":
            case "hash":
                return buildQueryPartStringPredicate(root.get(column), comparator, value, builder);
            case "mode":
                return buildQueryPartIntegerEnumPredicate(root.get(column), comparator, value, builder, OsuMode.values());
            case "mapper":
                Predicate creatorId = buildQueryPartIntegerPredicate(root.get("creatorId"), comparator, value, builder);
                Predicate creatorName = buildQueryPartStringPredicate(root.get("creatorName"), comparator, value, builder);
                if (creatorId != null && creatorName != null) {
                    return builder.or(creatorId, creatorName);
                } else {
                    return creatorId != null ? creatorId : creatorName;
                }
            case "status":
                return buildQueryPartIntegerEnumPredicate(root.get(column), comparator, value, builder, OsuStatus.values());
            case "stars":
            case "bpm":
            case "pp":
            case "cs":
            case "hp":
            case "od":
            case "ar":
            case "aim":
            case "speed":
            case "rating":
                return buildQueryPartDoublePredicate(root.get(column), comparator, value, builder);
            case "genre":
                return buildQueryPartIntegerEnumPredicate(root.get(column), comparator, value, builder, OsuGenre.values());
            case "language":
                return buildQueryPartIntegerEnumPredicate(root.get(column), comparator, value, builder, OsuLanguage.values());
            case "text":
                return builder.or(buildQueryPartStringPredicate(root.get("artist"), comparator, value, builder),
                        buildQueryPartStringPredicate(root.get("title"), comparator, value, builder),
                        buildQueryPartStringPredicate(root.get("version"), comparator, value, builder),
                        buildQueryPartStringPredicate(root.get("creatorName"), comparator, value, builder),
                        buildQueryPartStringPredicate(root.get("source"), comparator, value, builder),
                        buildQueryPartStringPredicate(root.get("tags"), comparator, value, builder));
            default:
                return null;
        }
    }

    private String getColumnName(String attribute) {
        switch (attribute) {
            case "b":
                return "beatmapId";
            case "s":
                return "beatmapsetId";
            case "artist":
                return "artist";
            case "title":
            case "text":
                return "title";
            case "diff":
                return "version";
            case "mode":
                return "mode";
            case "mapper":
                return "creatorName";
            case "status":
                return "approved";
            case "stars":
                return "difficultyRating";
            case "length":
                return "totalLength";
            case "drain":
                return "hitLength";
            case "bpm":
                return "bpm";
            case "combo":
                return "maxCombo";
            case "pp":
                return "maxPp";
            case "circles":
                return "countNormal";
            case "sliders":
                return "countSlider";
            case "spinners":
                return "countSpinner";
            case "cs":
                return "difficultySize";
            case "hp":
                return "difficultyDrain";
            case "od":
                return "difficultyOverall";
            case "ar":
                return "difficultyApproach";
            case "aim":
                return "difficultyAim";
            case "speed":
                return "difficultySpeed";
            case "rating":
                return "rating";
            case "favs":
                return "favouriteCount";
            case "plays":
                return "playCount";
            case "pass":
                return "passCount";
            case "source":
                return "source";
            case "genre":
                return "genre";
            case "language":
                return "language";
            case "tag":
                return "tags";
            case "hash":
                return "fileMd5";
            default:
                return null;
        }
    }

    private Predicate buildQueryPartDoublePredicate(
            Path<Double> column, String comparator, String input, CriteriaBuilder builder
    ) {
        double value;
        try {
            value = Double.parseDouble(input);
        } catch (NumberFormatException ignore) {
            return null;
        }
        switch (comparator) {
            case "=":
            case "==":
                return builder.and(
                        builder.greaterThanOrEqualTo(column, value - 0.005),
                        builder.lessThan(column, value + 0.005)
                );
            case "!=":
            case "<>":
                return builder.or(
                        builder.lessThan(column, value - 0.005),
                        builder.greaterThanOrEqualTo(column, value + 0.005)
                );
            case "<":
                return builder.lessThan(column, value - 0.005);
            case "<=":
                return builder.lessThan(column, value + 0.005);
            case ">":
                return builder.greaterThanOrEqualTo(column, value + 0.005);
            case ">=":
                return builder.greaterThanOrEqualTo(column, value - 0.005);
            default:
                return null;
        }
    }

    private Predicate buildQueryPartIntegerPredicate(
            Path<Integer> column, String comparator, String input, CriteriaBuilder builder
    ) {
        int value;
        try {
            value = Integer.parseInt(input);
        } catch (NumberFormatException ignore) {
            return null;
        }
        switch (comparator) {
            case "=":
            case "==":
                return builder.equal(column, value);
            case "!=":
            case "<>":
                return builder.notEqual(column, value);
            case "<":
                return builder.lessThan(column, value);
            case "<=":
                return builder.lessThanOrEqualTo(column, value);
            case ">":
                return builder.greaterThan(column, value);
            case ">=":
                return builder.greaterThanOrEqualTo(column, value);
            default:
                return null;
        }
    }

    private Predicate buildQueryPartIntegerEnumPredicate(
            Path<Integer> column, String comparator, String input, CriteriaBuilder builder, OsuEnum[] values
    ) {
        Integer value = Finder.findAnyLenient(values, mode -> new String[]{
                Integer.toString(mode.getId()),
                mode.getName(),
                mode.name()
        }, input).stream().findFirst().map(OsuEnum::getId).orElse(null);
        if (value == null) return null;
        switch (comparator) {
            case "=":
            case "==":
                return builder.equal(column, value);
            case "!=":
            case "<>":
                return builder.notEqual(column, value);
            case "<":
                return builder.lessThan(column, value);
            case "<=":
                return builder.lessThanOrEqualTo(column, value);
            case ">":
                return builder.greaterThan(column, value);
            case ">=":
                return builder.greaterThanOrEqualTo(column, value);
            default:
                return null;
        }
    }

    private Predicate buildQueryPartStringPredicate(
            Path<String> column, String comparator, String value, CriteriaBuilder builder
    ) {
        switch (comparator) {
            case "=":
            case "==":
                return builder.like(builder.lower(column), '%' + value.toLowerCase() + '%');
            case "!=":
            case "<>":
                return builder.notLike(builder.lower(column), '%' + value.toLowerCase() + '%');
            default:
                return null;
        }
    }

    private Sort.Order getSortOrder(String query) {
        Matcher matcher = PATTERN_SORT_PART.matcher(query);
        if (matcher.find()) {
            String column = getColumnName(matcher.group(3));
            String comparator = matcher.group(2);
            if (StringUtils.isNotEmpty(column) && StringUtils.isNotEmpty(comparator)) {
                switch (comparator) {
                    case "=":
                    case "==":
                    case "<":
                    case "<=":
                        return Sort.Order.asc(column);
                    case "!=":
                    case "<>":
                    case ">":
                    case ">=":
                        return Sort.Order.desc(column);
                    default:
                        break;
                }
            }
        }
        return Sort.Order.desc("approvedDate");
    }

}
