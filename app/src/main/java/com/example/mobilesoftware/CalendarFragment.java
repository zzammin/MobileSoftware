package com.example.mobilesoftware;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnRangeSelectedListener;
import com.prolificinteractive.materialcalendarview.format.ArrayWeekDayFormatter;
import com.prolificinteractive.materialcalendarview.format.MonthArrayTitleFormatter;
import com.prolificinteractive.materialcalendarview.format.TitleFormatter;
import com.prolificinteractive.materialcalendarview.format.WeekDayFormatter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.time.format.DateTimeFormatter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalendarFragment extends Fragment {
    private final String TAG = this.getClass().getSimpleName();

    MaterialCalendarView calendarView;
    TextView dateText;
    ListView listView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.calendar_fragment, container, false);

        calendarView = rootView.findViewById(R.id.calendarView);
        dateText = rootView.findViewById(R.id.dateText);
        listView = rootView.findViewById(R.id.dayMealList);

        // 월, 요일을 한글로 보이게 설정
        CharSequence[] customMonths = getResources().getTextArray(R.array.custom_months);
        String[] stringMonths = new String[customMonths.length];
        for (int i = 0; i < customMonths.length; i++) {
            stringMonths[i] = customMonths[i].toString();
        }

        if (stringMonths.length == 12) {
            calendarView.setTitleFormatter(new MonthArrayTitleFormatter(stringMonths));
        } else {
            Log.e("CalendarFragment", "Custom months array must have 12 labels.");
        }

        calendarView.setWeekDayFormatter(new ArrayWeekDayFormatter(getResources().getTextArray(R.array.custom_weekdays)));

        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                // 선택한 날짜를 Calendar 객체로 얻기
                Calendar selectedCalendar = date.getCalendar();

                // Calendar 객체를 SimpleDateFormat을 사용하여 원하는 형식의 문자열로 변환
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy년 MM월 dd일");
                String formattedDate = formatter.format(selectedCalendar.getTime());

                // TextView에 선택한 날짜 설정
                dateText.setText(formattedDate);

                // 여기서 선택한 날짜에 대한 추가적인 작업을 수행할 수 있습니다.
            }
        });

        // 좌우 화살표 가운데의 연/월이 보이는 방식 커스텀
        calendarView.setTitleFormatter(new TitleFormatter() {
            @Override
            public CharSequence format(CalendarDay day) {
                // Calendar 객체에서 LocalDate로 변환
                Calendar calendar = day.getCalendar();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH) + 1; // Calendar의 월은 0부터 시작하므로 1을 더해줍니다.

                // 연/월 문자열 생성
                StringBuilder calendarHeaderBuilder = new StringBuilder();
                calendarHeaderBuilder.append(year)
                        .append("년 ")
                        .append(month)
                        .append("월");
                return calendarHeaderBuilder.toString();
            }
        });

        return rootView;
    }

    List<com.example.mobilesoftware.MealItem> mealItems; // MealItem은 식사 데이터를 나타내는 클래스로 가정합니다

    public void setMealItems(List<com.example.mobilesoftware.MealItem> mealItems) {
        this.mealItems = mealItems;
        // ListView를 필터된 데이터로 업데이트하는 메서드 호출
        updateListView();
    }

    private void updateListView() {
        // 캘린더에서 선택한 날짜를 가져옵니다.
        Calendar selectedDate = calendarView.getSelectedDate().getCalendar();
        int year = selectedDate.get(Calendar.YEAR);
        int month = selectedDate.get(Calendar.MONTH) + 1;
        int day = selectedDate.get(Calendar.DAY_OF_MONTH);

        // 지정된 조건에 따라 식사 항목을 필터링합니다.
        List<String> filteredMeals = new ArrayList<>();
        for (com.example.mobilesoftware.MealItem meal : mealItems) {
            int mealHour = Integer.parseInt(meal.getHour());
            int mealMinute = Integer.parseInt(meal.getMinute());

            if ((mealHour <= 11 && mealMinute <= 30) ||
                    (mealHour >= 11 && mealHour < 14 && mealMinute >= 30) ||
                    (mealHour >= 17 && mealHour < 18)) {
                filteredMeals.add(meal.getMealName());
            }
        }

        // 필터된 데이터로 ListView를 업데이트합니다.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(), android.R.layout.simple_list_item_1, filteredMeals);
        listView.setAdapter(adapter);
    }
}
