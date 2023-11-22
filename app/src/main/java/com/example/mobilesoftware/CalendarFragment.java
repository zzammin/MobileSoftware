package com.example.mobilesoftware;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.format.ArrayWeekDayFormatter;
import com.prolificinteractive.materialcalendarview.format.MonthArrayTitleFormatter;
import com.prolificinteractive.materialcalendarview.format.TitleFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CalendarFragment extends Fragment {

    // 날짜별 데이터를 저장하는 맵
    private Map<CalendarDay, List<String>> dataMap = new HashMap<>();
    MaterialCalendarView calendarView;
    TextView dateText;
    ListView listView;

    String mealName;

    // 초기에는 빈 데이터 리스트로 어댑터를 설정
    List<String> dataList = new ArrayList<>();
    ListAdapter adapter = new ListAdapter(dataList);

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

                // 선택한 날짜에 해당하는 데이터를 리스트뷰에 표시
                updateListViewForDate(date, getArguments());
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

        // 초기에는 빈 데이터 리스트로 어댑터를 설정
        listView.setAdapter(adapter);

        return rootView;
    }

    private void updateListViewForDate(CalendarDay date, Bundle bundle) {
        // 클릭한 날짜의 연, 월, 일 값 가져오기
        int year = date.getYear();
        int month = date.getMonth();
        int day = date.getDay();

        // CalendarDay 객체 생성
        CalendarDay selectedDate = CalendarDay.from(year, month, day);

        // 선택한 날짜에 해당하는 데이터를 가져옴
        List<String> dataList = dataMap.get(selectedDate);

        // 데이터가 없으면 빈 리스트로 초기화
        if (dataList == null) {
            dataList = new ArrayList<>();
        }

        // mealName이 null이 아니고 비어있지 않으면 dataList에 추가
        String mealName = bundle.getString("mealName");
        if (mealName != null && !mealName.isEmpty()) {
            dataList.add("Location: " + bundle.getString("selectedLocation") + ", Meal: " + mealName);
        }

        // 데이터를 맵에 저장
        dataMap.put(selectedDate, dataList);

        // 어댑터에 데이터 설정
        adapter.setData(dataList);
        adapter.notifyDataSetChanged();

        // 처음에는 리스트뷰를 숨기고, 데이터가 있을 때만 보이도록 설정
        if (!dataList.isEmpty()) {
            listView.setVisibility(View.VISIBLE);
        } else {
            listView.setVisibility(View.GONE);
        }
    }

    public class ListAdapter extends BaseAdapter {
        private List<String> data;
        private int maxItems = 3;

        public ListAdapter(List<String> data){
            this.data=data;
        }

        // ListAdapter에 데이터 설정하는 메서드 추가
        public void setData(List<String> data) {
            this.data = data;
        }

        @Override
        public int getCount() {
            // 어댑터의 getCount() 메서드를 통해 항목 수를 제한
            return Math.min(data.size(), maxItems);
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // 간단한 예제로 TextView를 사용하여 데이터를 표시
            TextView textView;
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                textView = new TextView(parent.getContext());
                textView.setTextSize(16); // 텍스트 크기 조절
                convertView = textView;
            } else {
                textView = (TextView) convertView;
            }

            // 데이터 설정
            String mealData = (String) getItem(position);

            // 텍스트 설정
            textView.setText(mealData);

            return convertView;
        }
    }
}
