package com.example.mobilesoftware;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "meal_database";
    private static final int DATABASE_VERSION = 1;

    // 테이블 및 열 이름
    private static final String TABLE_MEALS = "meals";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_LOCATION = "location";
    private static final String COLUMN_MEAL_NAME = "meal_name";
    private static final String COLUMN_MEAL_OPINION = "meal_opinion";
    private static final String COLUMN_YEAR = "year";
    private static final String COLUMN_MONTH = "month";
    private static final String COLUMN_DAY = "day";
    private static final String COLUMN_HOUR = "hour";
    private static final String COLUMN_MINUTE = "minute";
    private static final String COLUMN_COST = "cost";

    // 생성 쿼리
    private static final String CREATE_MEALS_TABLE = "CREATE TABLE " + TABLE_MEALS + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_LOCATION + " TEXT," +
            COLUMN_MEAL_NAME + " TEXT," +
            COLUMN_MEAL_OPINION + " TEXT," +
            COLUMN_YEAR + " TEXT," +
            COLUMN_MONTH + " TEXT," +
            COLUMN_DAY + " TEXT," +
            COLUMN_HOUR + " TEXT," +
            COLUMN_MINUTE + " TEXT," +
            COLUMN_COST + " TEXT);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_MEALS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEALS);
        onCreate(db);
    }

    // 데이터베이스에 새로운 식사 기록 추가
    public long addMeal(String location, String mealName, String mealOpinion, String year, String month, String day, String hour, String minute, String cost) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_LOCATION, location);
        values.put(COLUMN_MEAL_NAME, mealName);
        values.put(COLUMN_MEAL_OPINION, mealOpinion);
        values.put(COLUMN_YEAR, year);
        values.put(COLUMN_MONTH, month);
        values.put(COLUMN_DAY, day);
        values.put(COLUMN_HOUR, hour);
        values.put(COLUMN_MINUTE, minute);
        values.put(COLUMN_COST, cost);

        long result = db.insert(TABLE_MEALS, null, values);
        db.close();
        return result;
    }

    // 모든 식사 기록 가져오기
    public Cursor getAllMeals() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_MEALS, null, null, null, null, null, null);
    }

    // DatabaseHelper 클래스에 getMealsForDate 메서드 추가
    public List<Meal> getMealsForDate(int year, int month, int day) {
        List<Meal> mealList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // 해당 날짜에 해당하는 데이터를 쿼리
        String[] columns = {
                COLUMN_LOCATION,
                COLUMN_MEAL_NAME,
                COLUMN_MEAL_OPINION,
                COLUMN_YEAR,
                COLUMN_MONTH,
                COLUMN_DAY,
                COLUMN_HOUR,
                COLUMN_MINUTE,
                COLUMN_COST
        };

        String selection = COLUMN_YEAR + " = ? AND " +
                COLUMN_MONTH + " = ? AND " +
                COLUMN_DAY + " = ?";

        String[] selectionArgs = {String.valueOf(year), String.valueOf(month), String.valueOf(day)};

        Cursor cursor = db.query(
                TABLE_MEALS,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            int locationIndex = cursor.getColumnIndex(COLUMN_LOCATION);
            int mealNameIndex = cursor.getColumnIndex(COLUMN_MEAL_NAME);
            int mealOpinionIndex = cursor.getColumnIndex(COLUMN_MEAL_OPINION);
            int mealYearIndex = cursor.getColumnIndex(COLUMN_YEAR);
            int mealMonthIndex = cursor.getColumnIndex(COLUMN_MONTH);
            int mealDayIndex = cursor.getColumnIndex(COLUMN_DAY);
            int mealHourIndex = cursor.getColumnIndex(COLUMN_HOUR);
            int mealMinuteIndex = cursor.getColumnIndex(COLUMN_MINUTE);
            int mealCostIndex = cursor.getColumnIndex(COLUMN_COST);

            do {
                // 열의 인덱스를 통해 데이터를 가져옴
                String location = cursor.getString(locationIndex);
                String mealName = cursor.getString(mealNameIndex);
                String mealOpinion = cursor.getString(mealOpinionIndex);
                String mealYear = cursor.getString(mealYearIndex);
                String mealMonth = cursor.getString(mealMonthIndex);
                String mealDay = cursor.getString(mealDayIndex);
                String mealHour = cursor.getString(mealHourIndex);
                String mealMinute = cursor.getString(mealMinuteIndex);
                String mealCost = cursor.getString(mealCostIndex);

                Meal meal = new Meal(location, mealName, mealOpinion, mealYear, mealMonth, mealDay, mealHour, mealMinute, mealCost);
                mealList.add(meal);
            } while (cursor.moveToNext());

            cursor.close();
        }

        // 디버그 문 추가
        Log.d("databaseHelper", "getMealsForDate: " + mealList.size() + " meals found for " + year + "-" + month + "-" + day);


        return mealList;
    }
}

