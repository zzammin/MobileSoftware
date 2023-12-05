package com.example.mobilesoftware;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalendarFragment extends Fragment {

    MaterialCalendarView calendarView;
    TextView dateText;
    ListView listView;

    // 초기에는 빈 데이터 리스트로 어댑터를 설정
    List<Meal> dataList = new ArrayList<>();
    ListAdapter adapter = new ListAdapter(dataList);

    // DatabaseHelper 객체 추가
    private DatabaseHelper databaseHelper;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.calendar_fragment, container, false);

        calendarView = rootView.findViewById(R.id.calendarView);
        dateText = rootView.findViewById(R.id.dateText);
        listView = rootView.findViewById(R.id.dayMealList);

        // DatabaseHelper 초기화
        databaseHelper = new DatabaseHelper(requireContext());

        // 어댑터 설정
        listView.setAdapter(adapter);

        getParentFragmentManager().setFragmentResultListener("inputData", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                // 값이 전달되었을 때의 처리를 수행
                updateListViewForDate(calendarView.getSelectedDate());


            }
        });

        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                // 선택한 날짜에 해당하는 데이터를 리스트뷰에 표시
                updateListViewForDate(date);
            }
        });

        // 처음에는 리스트뷰를 숨기고, 데이터가 있을 때만 보이도록 설정
        listView.setVisibility(View.GONE);


        return rootView;
    }

    private void updateListViewForDate(CalendarDay date) {
        // 클릭한 날짜의 연, 월, 일 값 가져오기
        int year = date.getYear();
        int month = date.getMonth() + 1;
        int day = date.getDay();

        // 데이터베이스에서 해당 날짜에 해당하는 데이터를 가져옴
        dataList = databaseHelper.getMealsForDate(year, month, day);

        // 데이터가 없으면 빈 리스트로 초기화
        if (dataList == null) {
            dataList = new ArrayList<>();
        }

        // 어댑터에 데이터 설정
        adapter.setData(dataList);
        adapter.notifyDataSetChanged();

        // TextView에 선택한 날짜 설정
        dateText.setText(String.format("%d년 %02d월 %02d일", year, month, day));
        dateText.setTextColor(Color.rgb(255,255,255));


        // 처음에는 리스트뷰를 숨기고, 데이터가 있을 때만 보이도록 설정
        if (!dataList.isEmpty()) {
            listView.setVisibility(View.VISIBLE);
        } else {
            listView.setVisibility(View.GONE);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Meal selectedMeal = dataList.get(position);

                // Open MealFragment with selected Meal
                openMealFragment(selectedMeal);
            }
        });


    }

    private void openMealFragment(Meal meal) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        MealFragment mealFragment = MealFragment.newInstance(meal);
        transaction.replace(R.id.fragment_linear, mealFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public class ListAdapter extends BaseAdapter {
        private List<Meal> data;

        public ListAdapter(List<Meal> data) {
            this.data = data;
        }

        // ListAdapter에 데이터 설정하는 메서드 추가
        public void setData(List<Meal> data) {
            this.data = data;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        // ListAdapter 클래스의 getView 메서드 수정
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // 간단한 예제로 TextView를 사용하여 데이터를 표시
            TextView textView;
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                textView = new TextView(parent.getContext());
                textView.setTextSize(20); // 텍스트 크기 조절
                textView.setTextColor(Color.WHITE);
                convertView = textView;
            } else {
                textView = (TextView) convertView;
            }

            // 데이터 설정
            Meal meal = (Meal) getItem(position);

            // 텍스트 설정
            if ("식사".equals(meal.getMealType())) {
                textView.setText(meal.getMealTime() + " - " + meal.getMealName());
            } else if ("음료".equals(meal.getMealType())) {
                textView.setText(meal.getMealType() + " - " + meal.getMealName());
            }

            return convertView;
        }
    }

}
