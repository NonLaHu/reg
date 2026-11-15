import json
import mariadb

DB_CONFIG = {
    "user": "student_reg",
    "password": "utycc2025",
    "host": "localhost",
    "port": 3306,
    "database": "student_reg"
}

with open("nrc.json", "r", encoding="utf-8") as f:
    data = json.load(f)

try:
    conn = mariadb.connect(**DB_CONFIG)
    cursor = conn.cursor()

    cursor.execute("""
    CREATE TABLE IF NOT EXISTS nrc_types (
        id VARCHAR(50) PRIMARY KEY,
        name_en VARCHAR(50) NOT NULL,
        name_mm VARCHAR(50) NOT NULL
    )
    """)

    cursor.execute("""
    CREATE TABLE IF NOT EXISTS nrc_states (
        id VARCHAR(50) PRIMARY KEY,
        code VARCHAR(50) NOT NULL,
        number_en VARCHAR(10) NOT NULL,
        number_mm VARCHAR(10) NOT NULL,
        name_en VARCHAR(50) NOT NULL,
        name_mm VARCHAR(50) NOT NULL
    )
    """)

    cursor.execute("""
    CREATE TABLE IF NOT EXISTS nrc_townships (
        id VARCHAR(50) PRIMARY KEY,
        code VARCHAR(50) NOT NULL,
        short_en VARCHAR(50),
        short_mm VARCHAR(50),
        name_en VARCHAR(50) NOT NULL,
        name_mm VARCHAR(50) NOT NULL,
        state_id VARCHAR(50) NOT NULL,
        state_code VARCHAR(10),
        FOREIGN KEY (state_id) REFERENCES nrc_states(id)
    )
    """)

    for t in data["nrcTypes"]:
        cursor.execute("""
        INSERT INTO nrc_types (id, name_en, name_mm)
        VALUES (?, ?, ?)
        ON DUPLICATE KEY UPDATE name_en=VALUES(name_en), name_mm=VALUES(name_mm)
        """, (t["id"], t["name"]["en"], t["name"]["mm"]))

    for s in data["nrcStates"]:
        cursor.execute("""
        INSERT INTO nrc_states (id, code, number_en, number_mm, name_en, name_mm)
        VALUES (?, ?, ?, ?, ?, ?)
        ON DUPLICATE KEY UPDATE code=VALUES(code), number_en=VALUES(number_en), 
                                number_mm=VALUES(number_mm), name_en=VALUES(name_en), name_mm=VALUES(name_mm)
        """, (s["id"], s["code"], s["number"]["en"], s["number"]["mm"], s["name"]["en"], s["name"]["mm"]))

    for tp in data["nrcTownships"]:
        cursor.execute("""
        INSERT INTO nrc_townships (id, code, short_en, short_mm, name_en, name_mm, state_id, state_code)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        ON DUPLICATE KEY UPDATE code=VALUES(code), short_en=VALUES(short_en),
                                short_mm=VALUES(short_mm), name_en=VALUES(name_en),
                                name_mm=VALUES(name_mm), state_id=VALUES(state_id), state_code=VALUES(state_code)
        """, (tp["id"], tp["code"], tp["short"]["en"], tp["short"]["mm"],
              tp["name"]["en"], tp["name"]["mm"], tp["stateId"], tp["stateCode"]))

    conn.commit()
    print("Data inserted successfully!")

except mariadb.Error as e:
    print(f"MariaDB error: {e}")
finally:
    if conn:
        conn.close()
