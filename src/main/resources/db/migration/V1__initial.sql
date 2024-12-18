CREATE TABLE IF NOT EXISTS students
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS courses
(
    id               SERIAL PRIMARY KEY,
    name             VARCHAR(255)   NOT NULL,
    description      TEXT,
    price            NUMERIC(10, 2) NOT NULL,
    duration         INT            NOT NULL,
    start_date       TIMESTAMP      NOT NULL,
    last_update_date TIMESTAMP      NOT NULL,
    is_open          BOOLEAN        NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS m2m_student_course
(
    student_id INT REFERENCES students (id) ON DELETE CASCADE,
    course_id  INT REFERENCES courses (id) ON DELETE CASCADE,
    PRIMARY KEY (student_id, course_id)
);

DO
$$
    BEGIN
        FOR i IN 1..1000
            LOOP
                INSERT INTO courses(name, description, price, duration, start_date, last_update_date, is_open)
                VALUES ('Course ' || i,
                        'Description for course ' || i,
                        random() * 100,
                        floor(random() * 100) + 10,
                        now() + (floor(random() * 30 + 1)) * INTERVAL '1 day',
                        now(),
                        true);
            END LOOP;
    END
$$;

DO
$$
    BEGIN
        FOR i in 1..500
            LOOP
                INSERT INTO students(name) VALUES ('Student ' || i);
            END LOOP;
    END;
$$;

WITH student_counts AS (SELECT c.id                  AS course_id,
                               30 + (random() * 120) AS student_count
                        FROM courses c),
     course_student_enrollments AS (SELECT c.id AS course_id,
                                           s.id AS student_id
                                    FROM courses c
                                             JOIN student_counts sc ON c.id = sc.course_id
                                             CROSS JOIN students s
                                    WHERE s.id IN (SELECT s2.id
                                                   FROM students s2
                                                   ORDER BY random()
                                                   LIMIT sc.student_count))
INSERT
INTO m2m_student_course (course_id, student_id)
SELECT course_id,
       student_id
FROM course_student_enrollments
ORDER BY course_id, student_id;

CREATE OR REPLACE PROCEDURE close_today_courses()
    LANGUAGE plpgsql
AS
$$
BEGIN
    UPDATE courses c
    SET is_open = false
    WHERE c.start_date::DATE = current_date
      AND (SELECT count(*)
           FROM m2m_student_course m
           WHERE m.course_id = c.id) < 30;
END;
$$;

