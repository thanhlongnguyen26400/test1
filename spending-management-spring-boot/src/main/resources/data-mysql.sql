INSERT INTO `roles` (`id`, `created_at`, `created_by`, `description`, `last_modified_at`, `last_modified_by`, `name`,
                     `valid_flag`, `version`)
VALUES ('1', '0', '0', 'admin role', '0', '0', 'ROLE_ADMIN', '1', '1'),
       ('2', '0', '0', 'user role', '0', '0', 'ROLE_USER', '1', '1');
INSERT INTO `users` (`id`, `created_at`, `created_by`, `description`, `ip`, `is_account_non_expired_alias`,
                     `is_account_non_locked_alias`, `is_credentials_non_expired_alias`, `is_enabled_alias`,
                     `last_login_at`, `last_modified_at`, `last_modified_by`, `name`, `pwd`, `usr`, `valid_flag`,
                     `version`, `role_id`)
VALUES ('0', '1467037490081', '0', 'root', 'true', b'1', b'1', b'1', b'1', '1487606227598', '1467037490081', '0',
        'root', 'root', 'root', '1', '1', '1');
INSERT INTO `accounts` (`id`, `account`, `account_source_type`, `created_at`, `created_by`, `last_modified_at`,
                        `last_modified_by`, `version`, `user_id`)
VALUES ('0', 'root', '0', '1467037490081', '0', '1467037490081', '0', '1', '0');
INSERT INTO `spending_manager`.`user_profiles` (`user_id`, `created_at`, `created_by`, `last_modified_at`,
                                                `last_modified_by`, `usr`, `valid_flag`, `version`)
VALUES ('0', '0', '0', '0', '0', 'root', '1', '1');


INSERT INTO `spending_manager`.`history_type`
(`id`,
 `color`,
 `created_at`,
 `created_by`,
 `description`,
 `last_modified_at`,
 `last_modified_by`, `name`, `valid_flag`)
VALUES (1, '#4ACCCD', 1467037490081, 0, 'Meet', 1467037490081, 0, 'Food', 1),
       (2, '#e2e650', 1467037490081, 0, 'Noodle', 1467037490081, 0, 'Food', 1),
       (3, '#3ce253', 1467037490081, 0, 'Coke', 1467037490081, 0, 'Drink', 1),
       (4, '#03aaf2', 1467037490081, 0, 'Pepsi', 1467037490081, 0, 'Drink', 1);

