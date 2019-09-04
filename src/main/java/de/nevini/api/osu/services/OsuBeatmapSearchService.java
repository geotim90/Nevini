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

    private static final Pattern PATTERN_CACHED_PART = Pattern.compile("(b|s|mapper|hash)==?(\\S+)");
    private static final Pattern PATTERN_QUERY_PART =
            Pattern.compile("(?:([a-z]+)([=<>]=?|!=|<>))?(\\S+)", Pattern.CASE_INSENSITIVE);

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
                PageRequest.of(0, 10, Sort.by(Sort.Order.desc("approvedDate"), Sort.Order.desc("lastUpdate"),
                        Sort.Order.asc("mode"), Sort.Order.asc("difficultyRating")))
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
        switch (attribute) {
            case "b":
                return buildQueryPartIntegerPredicate(root.get("beatmapId"), comparator, value, builder);
            case "s":
                return buildQueryPartIntegerPredicate(root.get("beatmapsetId"), comparator, value, builder);
            case "artist":
                return buildQueryPartStringPredicate(root.get("artist"), comparator, value, builder);
            case "title":
                return buildQueryPartStringPredicate(root.get("title"), comparator, value, builder);
            case "diff":
                return buildQueryPartStringPredicate(root.get("version"), comparator, value, builder);
            case "mode":
                return buildQueryPartIntegerEnumPredicate(root.get("mode"), comparator, value, builder, OsuMode.values());
            case "mapper":
                Predicate creatorId = buildQueryPartIntegerPredicate(root.get("creatorId"), comparator, value, builder);
                Predicate creatorName = buildQueryPartStringPredicate(root.get("creatorName"), comparator, value, builder);
                if (creatorId != null && creatorName != null) {
                    return builder.or(creatorId, creatorName);
                } else {
                    return creatorId != null ? creatorId : creatorName;
                }
            case "status":
                return buildQueryPartIntegerEnumPredicate(root.get("approved"), comparator, value, builder, OsuStatus.values());
            case "stars":
                return buildQueryPartDoublePredicate(root.get("difficultyRating"), comparator, value, builder);
            case "length":
                return buildQueryPartIntegerPredicate(root.get("totalLength"), comparator, value, builder);
            case "drain":
                return buildQueryPartIntegerPredicate(root.get("hitLength"), comparator, value, builder);
            case "bpm":
                return buildQueryPartDoublePredicate(root.get("bpm"), comparator, value, builder);
            case "combo":
                return buildQueryPartIntegerPredicate(root.get("maxCombo"), comparator, value, builder);
            case "pp":
                return buildQueryPartDoublePredicate(root.get("maxPp"), comparator, value, builder);
            case "circles":
                return buildQueryPartIntegerPredicate(root.get("countNormal"), comparator, value, builder);
            case "sliders":
                return buildQueryPartIntegerPredicate(root.get("countSlider"), comparator, value, builder);
            case "spinners":
                return buildQueryPartIntegerPredicate(root.get("countSpinner"), comparator, value, builder);
            case "cs":
                return buildQueryPartDoublePredicate(root.get("difficultySize"), comparator, value, builder);
            case "hp":
                return buildQueryPartDoublePredicate(root.get("difficultyDrain"), comparator, value, builder);
            case "od":
                return buildQueryPartDoublePredicate(root.get("difficultyOverall"), comparator, value, builder);
            case "ar":
                return buildQueryPartDoublePredicate(root.get("difficultyApproach"), comparator, value, builder);
            case "aim":
                return buildQueryPartDoublePredicate(root.get("difficultyAim"), comparator, value, builder);
            case "speed":
                return buildQueryPartDoublePredicate(root.get("difficultySpeed"), comparator, value, builder);
            case "rating":
                return buildQueryPartDoublePredicate(root.get("rating"), comparator, value, builder);
            case "favs":
                return buildQueryPartIntegerPredicate(root.get("favouriteCount"), comparator, value, builder);
            case "plays":
                return buildQueryPartIntegerPredicate(root.get("playCount"), comparator, value, builder);
            case "pass":
                return buildQueryPartIntegerPredicate(root.get("passCount"), comparator, value, builder);
            case "source":
                return buildQueryPartStringPredicate(root.get("source"), comparator, value, builder);
            case "genre":
                return buildQueryPartIntegerEnumPredicate(root.get("genre"), comparator, value, builder, OsuGenre.values());
            case "language":
                return buildQueryPartIntegerEnumPredicate(root.get("language"), comparator, value, builder, OsuLanguage.values());
            case "tag":
                return buildQueryPartStringPredicate(root.get("tags"), comparator, value, builder);
            case "hash":
                return buildQueryPartStringPredicate(root.get("fileMd5"), comparator, value, builder);
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

}
