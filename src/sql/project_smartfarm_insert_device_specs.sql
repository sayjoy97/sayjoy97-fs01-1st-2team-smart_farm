use project_smartfarm;

INSERT INTO device_specs (spec_uid, farm_slots, features)
VALUES (
  1, 1,
  JSON_OBJECT(
    'optimal_temp',       TRUE,
    'optimal_humidity',   TRUE,
    'light_intensity',    TRUE,
    'co2_level',    TRUE,
    'soil_moisture',      TRUE,
    'growth_period_days', TRUE
  )
);

INSERT INTO device_specs (spec_uid, farm_slots, features)
VALUES (
  2, 4,
  JSON_OBJECT(
    'optimal_temp',       TRUE,
    'optimal_humidity',   TRUE,
    'light_intensity',    TRUE,
    'co2_level',    TRUE,
    'soil_moisture',      TRUE,
    'growth_period_days', TRUE
  )
);

INSERT INTO device_specs (spec_uid, farm_slots, features)
VALUES (
  3, 1,
  JSON_OBJECT(
    'optimal_temp',       TRUE,
    'optimal_humidity',   TRUE,
    'light_intensity',    TRUE,
    'co2_level',    TRUE,
    'soil_moisture',      TRUE,
    'growth_period_days', FALSE
  )
);

INSERT INTO device_specs (spec_uid, farm_slots, features)
VALUES (
  4, 4,
  JSON_OBJECT(
    'optimal_temp',       TRUE,
    'optimal_humidity',   TRUE,
    'light_intensity',    TRUE,
    'co2_level',    TRUE,
    'soil_moisture',      TRUE,
    'growth_period_days', FALSE
  )
);
