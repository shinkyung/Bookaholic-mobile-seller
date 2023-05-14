package com.example.bookaholic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

public class Statistic extends AppCompatActivity {
    ArrayList<Order> orderC;

    ImageView returnBtn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);

        returnBtn = findViewById(R.id.returnStatistic);
        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        orderC = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("Users");
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.hasChildren()){
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        User user = userSnapshot.getValue(User.class);
                        if (user.getOrderHistory() == null){
                            continue;
                        }
                        for(Order order1: user.getOrderHistory()){
                            if(order1.getOrderStatus().contains("Complete")){
                                orderC.add(order1);
                            }
                        }
                    }
                    BarChart barChart = findViewById(R.id.barChart);
                    barChart.getAxisRight().setEnabled(false);
                    XAxis xAxis = barChart.getXAxis();
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    ArrayList<BarEntry> entries = new ArrayList<>();
                    int count = 0;
                    int day;
                    for (int i = 0; orderC.size() != 0;) {
                        float value = 0;
                        String[] split = orderC.get(i).getCreatedAt().split("/");
                        day = Integer.parseInt(split[0]);
                        String dateStr = orderC.get(i).getCreatedAt();
                        for (int j = i; j < orderC.size();j++){
                            count += 1;
                            if (dateStr.equalsIgnoreCase(orderC.get(j).getCreatedAt())){
                                value = value + orderC.get(j).getTotalPrice().floatValue() + count;
                                orderC.remove(j);
                                j--;
                            }
                        }

                        entries.add(new BarEntry(day,value));
                    }

                    BarDataSet dataSet = new BarDataSet(entries, null);
                    dataSet.setValueTextColor(Color.BLACK);
                    dataSet.setColors(Color.LTGRAY);

                    BarData barData = new BarData(dataSet);
                    barData.setBarWidth(0.9f);
                    barChart.setData(barData);

                    Description description = new Description();
                    description.setEnabled(false);
                    barChart.setDescription(description);

                    Legend legend = barChart.getLegend();
                    legend.setEnabled(false);

                    barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                        @Override
                        public void onValueSelected(Entry e, Highlight h) {
                            float x = h.getX();
                            float y = h.getY();

                            // Hiển thị thông tin chi tiết
                            Toast.makeText(Statistic.this, "Selected: x = " + x + ", y = " + y, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onNothingSelected() {
                        }
                    });

                    barChart.setFitBars(true);
                    barChart.invalidate();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors here
            }
        });
    }
}