package com.omnibot.subalterno;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

/** Recibe la alarma diaria y muestra la notificación de estudio. */
public class RecordatorioReceiver extends BroadcastReceiver {

    private static final String CANAL = "estudio_diario";

    @Override
    public void onReceive(Context ctx, Intent intent) {
        NotificationManager nm =
            (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel ch = new NotificationChannel(CANAL,
                "Recordatorio de estudio", NotificationManager.IMPORTANCE_DEFAULT);
            ch.setDescription("Aviso diario para mantener la racha de estudio");
            nm.createNotificationChannel(ch);
        }

        ProgressoRapido pr = new ProgressoRapido(ctx);
        String texto = pr.pendientes() > 0
            ? "Tienes " + pr.pendientes() + " preguntas pendientes de repaso. ¡Son 5 minutos!"
            : "Haz un test corto para mantener la racha de " + pr.racha() + " días.";

        Intent abrir = new Intent(ctx, MainActivity.class);
        abrir.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        int flags = PendingIntent.FLAG_UPDATE_CURRENT;
        if (Build.VERSION.SDK_INT >= 23) flags |= PendingIntent.FLAG_IMMUTABLE;
        PendingIntent pi = PendingIntent.getActivity(ctx, 2002, abrir, flags);

        Notification.Builder b = Build.VERSION.SDK_INT >= 26
            ? new Notification.Builder(ctx, CANAL)
            : new Notification.Builder(ctx);

        b.setSmallIcon(android.R.drawable.ic_menu_agenda)
         .setContentTitle("Hora de estudiar 📚")
         .setContentText(texto)
         .setStyle(new Notification.BigTextStyle().bigText(texto))
         .setContentIntent(pi)
         .setAutoCancel(true);

        nm.notify(1001, b.build());

        // reprogramar para mañana
        Intent rep = new Intent(ctx, RecordatorioReceiver.class);
        PendingIntent next = PendingIntent.getBroadcast(ctx, 1001, rep, flags);
        android.app.AlarmManager am =
            (android.app.AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
        long manana = System.currentTimeMillis() + 24 * 60 * 60 * 1000L;
        if (Build.VERSION.SDK_INT >= 23) {
            am.setAndAllowWhileIdle(android.app.AlarmManager.RTC_WAKEUP, manana, next);
        } else {
            am.set(android.app.AlarmManager.RTC_WAKEUP, manana, next);
        }
    }

    /** Acceso ligero al progreso sin levantar la UI. */
    static final class ProgressoRapido {
        private final Progreso p;
        ProgressoRapido(Context c) { p = new Progreso(c); }
        int pendientes() { return p.pendientesHoy(); }
        int racha() { return p.racha(); }
    }
}
