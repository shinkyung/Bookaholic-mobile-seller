package com.example.bookaholic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.example.bookaholic.orderHistory.OrderHistoryAdapter;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Statistic extends AppCompatActivity {
    ArrayList<Order> orderC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);

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
                            if(order1.getOrderStatus().contains("Incomplete")){
                                continue;
                            }
                            else{
                                orderC.add(order1);
                            }
                        }
                    }
                    LineChart lineChart = findViewById(R.id.lineChart);
                    System.out.println(orderC.size() + "Hello world");
                    ArrayList<Entry> entries = new ArrayList<>();

                    for (int i = 0; i < orderC.size(); i++) {
                        float value = orderC.get(i).getTotalPrice().floatValue();
                        String dateStr = orderC.get(i).getCreatedAt();
                        Date date = null;
                        try {
                            date = new SimpleDateFormat("dd/MM/yyyy").parse(dateStr);
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                        long timeMillis = date.getTime();
                        entries.add(new Entry(timeMillis, value));
                    }

                    LineDataSet dataSet = new LineDataSet(entries, "Label");
                    dataSet.setColor(Color.RED);
                    dataSet.setLineWidth(2f);

                    LineData lineData = new LineData(dataSet);
                    lineChart.setData(lineData);
                    XAxis xAxis = lineChart.getXAxis();

// Set the x-axis to display dates in the format "dd/MM/yyyy"
                    xAxis.setValueFormatter(new ValueFormatter() {
                        private final SimpleDateFormat mFormat = new SimpleDateFormat("dd/MM/yyyy");

                        @Override
                        public String getAxisLabel(float value, AxisBase axis) {
                            return mFormat.format(new Date((long) value));
                        }
                    });
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    lineChart.invalidate();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors here
            }
        });
    }

}