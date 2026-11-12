CREATE TABLE IF NOT EXISTS `enum_languages` (
                                                `language_id` TINYINT AUTO_INCREMENT,
                                                `code` VARCHAR(10) NOT NULL UNIQUE,
                                                `name` VARCHAR(50) NOT NULL,
                                                PRIMARY KEY(`language_id`)
);


CREATE TABLE IF NOT EXISTS `enum_theme_modes` (
                                                  `theme_mode_id` TINYINT AUTO_INCREMENT,
                                                  `code` VARCHAR(20) NOT NULL UNIQUE,
                                                  `name` VARCHAR(50) NOT NULL,
                                                  PRIMARY KEY(`theme_mode_id`)
);


CREATE TABLE IF NOT EXISTS `enum_accent_colors` (
                                                    `accent_color_id` TINYINT AUTO_INCREMENT,
                                                    `code` VARCHAR(30) NOT NULL UNIQUE,
                                                    `hex_value` VARCHAR(7) NOT NULL,
                                                    PRIMARY KEY(`accent_color_id`)
);


CREATE TABLE IF NOT EXISTS `users` (
                                       `user_id` BIGINT AUTO_INCREMENT UNIQUE,
                                       `name` VARCHAR(100) NOT NULL,
                                       `email` VARCHAR(255) NOT NULL UNIQUE,
                                       `password_hash` VARCHAR(128) NOT NULL UNIQUE,
                                       `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                       `active` BOOLEAN NOT NULL DEFAULT true,
                                       PRIMARY KEY(`user_id`)
);


CREATE TABLE IF NOT EXISTS `app_settings` (
                                              `user_id` BIGINT,
                                              `language_id` TINYINT NOT NULL DEFAULT 1,
                                              `theme_mode_id` TINYINT NOT NULL DEFAULT 3,
                                              `accent_color_id` TINYINT NOT NULL DEFAULT 1,
                                              `notifications_enabled` BOOLEAN NOT NULL DEFAULT true,
                                              PRIMARY KEY(`user_id`)
);


CREATE TABLE IF NOT EXISTS `meetings` (
                                          `meeting_id` BIGINT AUTO_INCREMENT,
                                          `title` VARCHAR(150) NOT NULL,
                                          `duration_minutes` INTEGER NOT NULL DEFAULT 60,
                                          `search_window_start` DATETIME NOT NULL,
                                          `search_window_end` DATETIME NOT NULL,
                                          `created_by` BIGINT NOT NULL,
                                          `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                          PRIMARY KEY(`meeting_id`)
);


CREATE TABLE IF NOT EXISTS `user_availability_slots` (
                                                         `slot_id` BIGINT AUTO_INCREMENT,
                                                         `user_id` BIGINT NOT NULL,
                                                         `meeting_id` BIGINT NOT NULL,
                                                         `start_time` DATETIME NOT NULL,
                                                         `end_time` DATETIME NOT NULL,
                                                         `priority` TINYINT NOT NULL DEFAULT 3,
                                                         `source` ENUM('manual', 'ics_import') NOT NULL DEFAULT 'manual',
                                                         PRIMARY KEY(`slot_id`)
);


ALTER TABLE `app_settings`
    ADD FOREIGN KEY(`user_id`) REFERENCES `users`(`user_id`)
        ON UPDATE NO ACTION ON DELETE CASCADE;
ALTER TABLE `app_settings`
    ADD FOREIGN KEY(`language_id`) REFERENCES `enum_languages`(`language_id`)
        ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE `app_settings`
    ADD FOREIGN KEY(`theme_mode_id`) REFERENCES `enum_theme_modes`(`theme_mode_id`)
        ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE `app_settings`
    ADD FOREIGN KEY(`accent_color_id`) REFERENCES `enum_accent_colors`(`accent_color_id`)
        ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE `meetings`
    ADD FOREIGN KEY(`created_by`) REFERENCES `users`(`user_id`)
        ON UPDATE NO ACTION ON DELETE CASCADE;
ALTER TABLE `user_availability_slots`
    ADD FOREIGN KEY(`user_id`) REFERENCES `users`(`user_id`)
        ON UPDATE NO ACTION ON DELETE CASCADE;
ALTER TABLE `user_availability_slots`
    ADD FOREIGN KEY(`meeting_id`) REFERENCES `meetings`(`meeting_id`)
        ON UPDATE NO ACTION ON DELETE CASCADE;