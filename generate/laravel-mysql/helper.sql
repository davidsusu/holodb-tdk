TRUNCATE TABLE coupons;

INSERT INTO log_table (message) VALUES ('Coupon fill started');

ALTER TABLE coupons MODIFY id BIGINT NOT NULL;
ALTER TABLE coupons DROP PRIMARY KEY;
ALTER TABLE coupons DROP INDEX idx_coupons_identifier;

SET FOREIGN_KEY_CHECKS = 0;
ALTER TABLE coupons DISABLE KEYS;

DROP PROCEDURE IF EXISTS populate_coupons;

DELIMITER //
CREATE PROCEDURE populate_coupons()
BEGIN
    DECLARE counter BIGINT DEFAULT 1;
    WHILE counter <= 10000000 DO
        INSERT INTO coupons (id, employee_id, identifier) VALUES (
            counter,
            (RAND()*1000000) + 1,
            CONCAT(
                CHAR(FLOOR(RAND()*6)+65),
                CHAR(FLOOR(RAND()*6)+65),
                CHAR(FLOOR(RAND()*6)+65),
                '-',
                LPAD(FLOOR(RAND() * POW(10, 12)), 12, '0')
            )
        );
        SET counter = counter + 1;
    END WHILE;
END;
//
DELIMITER ;

CALL populate_coupons();

DROP PROCEDURE populate_coupons;

SET FOREIGN_KEY_CHECKS = 1;
ALTER TABLE coupons ENABLE KEYS;

CREATE INDEX idx_coupons_identifier ON coupons(identifier);

ALTER TABLE coupons ADD PRIMARY KEY (id);
ALTER TABLE coupons MODIFY id BIGINT NOT NULL AUTO_INCREMENT;
ALTER TABLE coupons AUTO_INCREMENT = 10000001;
CREATE INDEX idx_coupons_identifier ON coupons(identifier);

INSERT INTO log_table (message) VALUES ('Coupon fill finished');


