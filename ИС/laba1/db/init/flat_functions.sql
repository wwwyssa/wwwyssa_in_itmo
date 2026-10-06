BEGIN;

-- Ties use the smallest ID so results remain deterministic.
CREATE OR REPLACE FUNCTION flat_max_area()
RETURNS SETOF flat
LANGUAGE sql STABLE
AS $$
    SELECT f.* FROM flat f ORDER BY f.area DESC, f.id ASC LIMIT 1;
$$;

-- NULL means flats without a house. Nonexistent house IDs give a count of zero.
CREATE OR REPLACE FUNCTION flat_count_by_house(p_house_id BIGINT)
RETURNS BIGINT
LANGUAGE sql STABLE
AS $$
    SELECT count(*) FROM flat f WHERE f.house_id IS NOT DISTINCT FROM p_house_id;
$$;

-- Prefixes are case-sensitive and literal, including %, _ and backslash.
CREATE OR REPLACE FUNCTION flat_names_starting_with(p_prefix TEXT)
RETURNS SETOF flat
LANGUAGE sql STABLE
AS $$
    SELECT f.* FROM flat f
    WHERE left(f.name, char_length(p_prefix)) = p_prefix
    ORDER BY f.id ASC;
$$;

-- An unknown balcony value (NULL) is not the same as no balcony (FALSE).
CREATE OR REPLACE FUNCTION flat_most_expensive_without_balcony()
RETURNS SETOF flat
LANGUAGE sql STABLE
AS $$
    SELECT f.* FROM flat f WHERE f.balcony IS FALSE
    ORDER BY f.price DESC, f.id ASC LIMIT 1;
$$;

CREATE OR REPLACE FUNCTION flat_most_expensive_of_three(p_id1 INTEGER, p_id2 INTEGER, p_id3 INTEGER)
RETURNS SETOF flat
LANGUAGE plpgsql STABLE
AS $$
BEGIN
    RETURN QUERY SELECT f.* FROM flat f WHERE f.id IN (p_id1, p_id2, p_id3)
        ORDER BY f.price DESC, f.id ASC LIMIT 1;
END;
$$;

COMMIT;
