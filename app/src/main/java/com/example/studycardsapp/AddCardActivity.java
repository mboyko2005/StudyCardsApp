package com.example.studycardsapp;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.room.Room;

import com.google.android.material.button.MaterialButton;
import com.example.studycardsapp.database.AppDatabase;
import com.example.studycardsapp.database.Card;

public class AddCardActivity extends AppCompatActivity {

    private EditText etQuestion;
    private EditText etAnswer;
    private MaterialButton btnSaveCard;
    private static final String TAG = "AddCardActivity";
    private static final String CHANNEL_ID = "study_cards_channel";
    private static final int REQUEST_CODE_POST_NOTIFICATIONS = 1;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cards);

        createNotificationChannel();

        etQuestion = findViewById(R.id.etQuestion);
        etAnswer = findViewById(R.id.etAnswer);
        btnSaveCard = findViewById(R.id.btnSaveCard);

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "cards-database")
                .allowMainThreadQueries()
                .build();

        btnSaveCard.setOnClickListener(view -> saveCard());

        // Проверка разрешений для уведомлений (Android 13 и выше)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQUEST_CODE_POST_NOTIFICATIONS);
            }
        }
    }

    private void saveCard() {
        String question = etQuestion.getText().toString().trim();
        String answer = etAnswer.getText().toString().trim();

        if (question.isEmpty() || answer.isEmpty()) {
            Toast.makeText(this, "Пожалуйста, заполните оба поля", Toast.LENGTH_SHORT).show();
            return;
        }

        Card newCard = new Card(question, answer);
        db.cardDao().insert(newCard);

        Toast.makeText(this, "Карточка сохранена", Toast.LENGTH_SHORT).show();
        etQuestion.setText("");
        etAnswer.setText("");

        // Отправка уведомления
        sendNotification("Новая карточка добавлена", "Вопрос: " + question);
    }

    private void createNotificationChannel() {
        // Создаем канал уведомлений для Android 8.0 и выше
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Канал учебных карточек";
            String description = "Канал для уведомлений учебных карточек";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) { // Добавлена проверка на null
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    private void sendNotification(String title, String content) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info) // Используем системную иконку
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true); // Убирает уведомление при нажатии

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // Проверка разрешений для уведомлений
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            notificationManager.notify(1001, builder.build());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_POST_NOTIFICATIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "POST_NOTIFICATIONS permission granted");
            } else {
                Log.d(TAG, "POST_NOTIFICATIONS permission denied");
                Toast.makeText(this, "Разрешение на уведомления отклонено", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
