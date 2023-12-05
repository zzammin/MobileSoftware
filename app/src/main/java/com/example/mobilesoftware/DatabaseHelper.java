package com.example.mobilesoftware;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "meal_database";
    private static final int DATABASE_VERSION = 4;

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
    private static final String COLUMN_CALORIE = "calorie";
    private static final String COLUMN_MEALTIME = "mealtime";
    private static final String COLUMN_MEAL_TYPE = "meal_type";
    private static final String COLUMN_MEAL_IMAGE_URI = "meal_image_uri"; // 이미지 URI를 저장할 열 추가

    private static final String CREATE_MEALS_TABLE = "CREATE TABLE " + TABLE_MEALS + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_LOCATION + " TEXT," +
            COLUMN_MEAL_NAME + " TEXT," +
            COLUMN_MEAL_OPINION + " TEXT," +
            COLUMN_YEAR + " INTEGER," +  // 변경: INTEGER로 수정
            COLUMN_MONTH + " INTEGER," + // 변경: INTEGER로 수정
            COLUMN_DAY + " INTEGER," +   // 변경: INTEGER로 수정
            COLUMN_HOUR + " TEXT," +
            COLUMN_MINUTE + " TEXT," +
            COLUMN_COST + " TEXT," +
            COLUMN_CALORIE + " INTEGER," +
            COLUMN_MEALTIME + " TEXT," +
            COLUMN_MEAL_TYPE + " TEXT," +
            COLUMN_MEAL_IMAGE_URI + " TEXT);"; // 이미지 URI를 저장할 열 추가

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

    public long addMeal(String location, String mealName, String mealOpinion, String year, String month, String day, String hour, String minute, String cost, int calorie, String mealType, String mealImageUri) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_LOCATION, location);
        values.put(COLUMN_MEAL_NAME, mealName);
        values.put(COLUMN_MEAL_OPINION, mealOpinion);
        values.put(COLUMN_YEAR, Integer.parseInt(year));
        values.put(COLUMN_MONTH, Integer.parseInt(month));
        values.put(COLUMN_DAY, Integer.parseInt(day));
        values.put(COLUMN_HOUR, hour);
        values.put(COLUMN_MINUTE, minute);
        values.put(COLUMN_COST, cost);
        values.put(COLUMN_CALORIE, calorie);
        values.put(COLUMN_MEAL_TYPE, mealType);
        values.put(COLUMN_MEAL_IMAGE_URI, mealImageUri); // 이미지 URI 추가

        // hour 값을 기반으로 mealtime 설정
        String mealtime = calculateMealtime(hour);
        values.put(COLUMN_MEALTIME, mealtime);

        long result = db.insert(TABLE_MEALS, null, values);
        db.close();
        return result;
    }

    public Cursor getAllMeals() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_MEALS, null, null, null, null, null, null);
    }

    public List<Meal> getMealsForDate(int year, int month, int day) {
        List<Meal> mealList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {
                COLUMN_LOCATION,
                COLUMN_MEAL_NAME,
                COLUMN_MEAL_OPINION,
                COLUMN_YEAR,
                COLUMN_MONTH,
                COLUMN_DAY,
                COLUMN_HOUR,
                COLUMN_MINUTE,
                COLUMN_COST,
                COLUMN_CALORIE,
                COLUMN_MEALTIME,
                COLUMN_MEAL_TYPE,
                COLUMN_MEAL_IMAGE_URI // 이미지 URI 열 추가
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
            int mealCalorieIndex = cursor.getColumnIndex(COLUMN_CALORIE);
            int mealtimeIndex = cursor.getColumnIndex(COLUMN_MEALTIME);
            int mealTypeIndex = cursor.getColumnIndex(COLUMN_MEAL_TYPE);
            int mealImageUriIndex = cursor.getColumnIndex(COLUMN_MEAL_IMAGE_URI); // 이미지 URI 열 인덱스 추가

            do {
                String location = cursor.getString(locationIndex);
                String mealName = cursor.getString(mealNameIndex);
                String mealOpinion = cursor.getString(mealOpinionIndex);
                String mealYear = cursor.getString(mealYearIndex);
                String mealMonth = cursor.getString(mealMonthIndex);
                String mealDay = cursor.getString(mealDayIndex);
                String mealHour = cursor.getString(mealHourIndex);
                String mealMinute = cursor.getString(mealMinuteIndex);
                String mealCost = cursor.getString(mealCostIndex);
                int mealCalorie = cursor.getInt(mealCalorieIndex);
                String mealtime = cursor.getString(mealtimeIndex);
                String mealType = cursor.getString(mealTypeIndex);
                String mealImageUri = cursor.getString(mealImageUriIndex); // 이미지 URI 가져오기

                Meal meal = new Meal(location, mealName, mealOpinion, mealYear, mealMonth, mealDay, mealHour, mealMinute, mealCost, mealCalorie, mealtime, mealType, mealImageUri);
                mealList.add(meal);
            } while (cursor.moveToNext());

            cursor.close();
        }

        Log.d("databaseHelper", "getMealsForDate: " + mealList.size() + " meals found for " + year + "-" + month + "-" + day);

        return mealList;
    }

    private String calculateMealtime(String hour) {
        int hourValue = Integer.parseInt(hour);
        if (hourValue >= 8 && hourValue < 11) {
            return "조식";
        } else if (hourValue >= 11 && hourValue < 17) {
            return "중식";
        } else if (hourValue >= 17 && hourValue <= 19) {
            return "석식";
        }
        return "";
    }

    public List<Meal> getMealsForDateRange(int startYear, int startMonth, int startDay, int endYear, int endMonth, int endDay) {
        List<Meal> mealList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {
                COLUMN_LOCATION,
                COLUMN_MEAL_NAME,
                COLUMN_MEAL_OPINION,
                COLUMN_YEAR,
                COLUMN_MONTH,
                COLUMN_DAY,
                COLUMN_HOUR,
                COLUMN_MINUTE,
                COLUMN_COST,
                COLUMN_CALORIE,
                COLUMN_MEALTIME,
                COLUMN_MEAL_TYPE,
                COLUMN_MEAL_IMAGE_URI // 이미지 URI 열 추가
        };

        String selection = "(" +
                COLUMN_YEAR + " = ? AND " +
                COLUMN_MONTH + " = ? AND " +
                COLUMN_DAY + " >= ?) OR (" +
                COLUMN_YEAR + " = ? AND " +
                COLUMN_MONTH + " > ?) OR (" +
                COLUMN_YEAR + " = ? AND " +
                COLUMN_MONTH + " = ? AND " +
                COLUMN_DAY + " <= ?)";

        String[] selectionArgs = {
                String.valueOf(startYear), String.valueOf(startMonth), String.valueOf(startDay),
                String.valueOf(startYear), String.valueOf(startMonth),
                String.valueOf(endYear), String.valueOf(endMonth), String.valueOf(endDay)
        };


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
            int mealCalorieIndex = cursor.getColumnIndex(COLUMN_CALORIE);
            int mealtimeIndex = cursor.getColumnIndex(COLUMN_MEALTIME);
            int mealTypeIndex = cursor.getColumnIndex(COLUMN_MEAL_TYPE);
            int mealImageUriIndex = cursor.getColumnIndex(COLUMN_MEAL_IMAGE_URI); // 이미지 URI 열 인덱스 추가

            do {
                String location = cursor.getString(locationIndex);
                String mealName = cursor.getString(mealNameIndex);
                String mealOpinion = cursor.getString(mealOpinionIndex);
                String mealYear = cursor.getString(mealYearIndex);
                String mealMonth = cursor.getString(mealMonthIndex);
                String mealDay = cursor.getString(mealDayIndex);
                String mealHour = cursor.getString(mealHourIndex);
                String mealMinute = cursor.getString(mealMinuteIndex);
                String mealCost = cursor.getString(mealCostIndex);
                int mealCalorie = cursor.getInt(mealCalorieIndex);
                String mealtime = cursor.getString(mealtimeIndex);
                String mealType = cursor.getString(mealTypeIndex);
                String mealImageUri = cursor.getString(mealImageUriIndex);

                Meal meal = new Meal(location, mealName, mealOpinion, mealYear, mealMonth, mealDay, mealHour, mealMinute, mealCost, mealCalorie, mealtime, mealType, mealImageUri);
                mealList.add(meal);
            } while (cursor.moveToNext());

            cursor.close();
        }

        Log.d("DatabaseHelper", "getMealsForDateRange: " + mealList.size() + " meals found from " + startYear + "-" + startMonth + "-" + startDay + " to " + endYear + "-" + endMonth + "-" + endDay);

        return mealList;
    }
}
