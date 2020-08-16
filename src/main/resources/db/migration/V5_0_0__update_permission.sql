UPDATE permission
SET node = 'warframe'
WHERE node = 'warframestat.us';

DELETE
FROM permission
WHERE node = 'warframe.order-book';

DELETE
FROM permission
WHERE node = 'warframe.price-check';
