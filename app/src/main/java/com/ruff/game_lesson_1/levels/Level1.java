package com.ruff.game_lesson_1.levels;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnPaidEventListener;
import com.google.android.gms.ads.ResponseInfo;
import com.google.android.gms.ads.admanager.AdManagerInterstitialAd;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.internal.ads.zzblb;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.slider.Slider;
import com.ruff.game_lesson_1.GameLevels;
import com.ruff.game_lesson_1.LivesSingleton;
import com.ruff.game_lesson_1.MyInterstitialAd;
import com.ruff.game_lesson_1.MyMediaPlayer;
import com.ruff.game_lesson_1.MyRewardedAd;
import com.ruff.game_lesson_1.R;
import com.ruff.game_lesson_1.databinding.UniversalBinding;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;


public class Level1 extends AppCompatActivity {

    private static final String SAVE_FILE = "SAVE_FILE";
    private static final String LEVEL_KEY = "LEVEL_KEY";
    private int levelCounter;
    SharedPreferences getProgress;
    SharedPreferences.Editor saveProgress;

    private UniversalBinding binding;

    Slider slider;
    Animation animation;
    int leftNumCard, rightNumCard;
    MyMediaPlayer soundEndDialog, soundLivesDialog;
    private LivesSingleton livesSingleton;
    private MyInterstitialAd myInterstitialAd;
    private MyRewardedAd myRewardedAd;

    private final int wrongAnswerPoint = 2;
    private final int trueAnswerPoint = 1;
    private final int oneLive = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = UniversalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Window w = getWindow();
        w.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION //Скрываем нижнюю панель навигации.
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY); //Появляется поверх игры и исчезает.

        //Запрещаем ночную тему.
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        //создаем синглтон с количеством жизней.
        livesSingleton = LivesSingleton.getInstance();

        //проверка на бесконечные жизни (покупка инапа в приложении) - начало
        if (livesSingleton.isEndlessLives()) {
            binding.tvHeartCounter.setText("∞");
        } else {
            binding.tvHeartCounter.setText(String.valueOf(livesSingleton.getCurrentLives()));
        }


        //Проверка на премиум доступ
        if (livesSingleton.isEndlessLives()) { //если есть премиум
            myInterstitialAd = null;
        } else { //если нет премиума
            //загрузка межстраничной рекламы в память
            myInterstitialAd = MyInterstitialAd.getInstance();
            myInterstitialAd.loadInterstitialAd(this);

            //загрузка рекламы с вознаграждением в память
            myRewardedAd = MyRewardedAd.getInstance();
            myRewardedAd.loadRewardedAd(this);
        }

        //инициализируем плеер для проигрывания в конце диалога
        soundEndDialog = new MyMediaPlayer(this, R.raw.sound_level_complete);

        //инициализируем плеер для проигрывания звука конца жизней
        soundLivesDialog = new MyMediaPlayer(this, R.raw.sound_level_fail);

        //обработка нажатия кнопки назад
        binding.btBack.setOnClickListener(v -> {
            onBackPressed();
        });

        //установка номера уровня
        binding.tvLevelNumber.setText(R.string.level_1);

        //скругление углов картинок
        binding.tvLeftNumber.setClipToOutline(true);
        binding.tvRightNumber.setClipToOutline(true);

        //вызов стартового диалогового окна
        initStartDialog();

        //метод инициализация цифр на карточках
        initCards();
    }


    /**
     * Метод вызова стартового диалогового окна
     * Появляеться перед началом уровня.
     */
    private void initStartDialog() {
        Dialog dialogStart = new Dialog(this);
        dialogStart.requestWindowFeature(Window.FEATURE_NO_TITLE); //диалог без названия
        dialogStart.setContentView(R.layout.preview_dialog); //выбор макета для диалога

        Window w = dialogStart.getWindow();
        w.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION //Скрываем нижнюю панель навигации.
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY); //Появляется поверх игры и исчезает.

        ConstraintLayout constraintLayout = dialogStart.findViewById(R.id.my_preview_dialog_constraint);
        constraintLayout.setBackgroundResource(R.drawable.im_back_dialog_level1); //выбор заднего фона для главного контейнера диалога
        ImageView ivDialog = dialogStart.findViewById(R.id.imageView);
        ivDialog.setImageResource(R.drawable.two_cards_level1);
        dialogStart.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogStart.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT); //растягивания диалога на экране
        dialogStart.setCancelable(false); //отключение системной кнопки назад
        dialogStart.show(); // отображение диалога


        MaterialButton close = dialogStart.findViewById(R.id.bt_close_dialog);
        close.setOnClickListener(v -> { //обработки кнопки закрытия диалога
            onBackPressed(); // вызов переопределенного метода
        });

        MaterialButton _continue = dialogStart.findViewById(R.id.bt_continue);
        _continue.setOnClickListener(v -> { //обработка нажатия кнопки "продолжить"
            dialogStart.cancel();
        });


    }


    /**
     * Метод вызова диалогового окна в конце уровня
     * Появляеться после успешного завершения уровня.
     */
    private void initEndDialog() {

        soundEndDialog.play(); //воспроизведение звука прохождения уровня
        Dialog dialogEnd = new Dialog(this);
        dialogEnd.requestWindowFeature(Window.FEATURE_NO_TITLE);  //диалог без названия
        dialogEnd.setContentView(R.layout.end_dialog); //выбор макета для диалога

        Window w = dialogEnd.getWindow();
        w.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION //Скрываем нижнюю панель навигации.
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY); //Появляется поверх игры и исчезает.

        ConstraintLayout constraintLayout = dialogEnd.findViewById(R.id.my_end_dialog_constraint);
        constraintLayout.setBackgroundResource(R.drawable.im_back_dialog_level1); //выбор заднего фона для главного контейнера диалога
        dialogEnd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //затемнение заднего фона диалога
        TextView tvTextDialogEnd = dialogEnd.findViewById(R.id.textView);
        tvTextDialogEnd.setText(getResources().getString(R.string.interesting_fact_level1));
        dialogEnd.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT); //растягивания диалога на экране
        dialogEnd.setCancelable(false); //отключение системной кнопки назад
        dialogEnd.show(); //отображение диалога

        MaterialButton close = dialogEnd.findViewById(R.id.bt_close_dialog);
        MaterialButton _continue = dialogEnd.findViewById(R.id.bt_continue);

        //обработка кнопки закрыть (крестик) - начало.
        close.setOnClickListener(v -> {
            soundEndDialog.stopPlay(); //завершение проигрывания звука

            //показ рекламы
            if (!livesSingleton.isEndlessLives() && myInterstitialAd.getLevelCompleteCounter() == myInterstitialAd.getMaxLevelComplete()) {
                myInterstitialAd.showInterstitialAd(null, Level1.this);
                myInterstitialAd.setLevelCompleteCounter(0);
            } else {
                onBackPressed();
            }

        });
        //обработка кнопки закрыть (крестик) - конец.


        //обработка кнопки продолжить - начало
        _continue.setOnClickListener(v -> {
            soundEndDialog.stopPlay(); //завершение проигрывания звука
            Intent intent = new Intent(Level1.this, Level2.class);

            if (!livesSingleton.isEndlessLives() && myInterstitialAd.getLevelCompleteCounter() == myInterstitialAd.getMaxLevelComplete()) {
                myInterstitialAd.showInterstitialAd(intent, Level1.this);
                myInterstitialAd.setLevelCompleteCounter(0);
            } else {
                startActivity(intent);
                finish();
            }

            dialogEnd.cancel();
        });
        //обработка кнопки продолжить - конец

    }


    /**
     * Метод вызова диалогового окна конца жизней
     * Появляеться после того как все жизни потрачены.
     * Пользователю предлогается посмотреть рекламу или купить премиум.
     */
    private void initLivesDialog() {
        soundLivesDialog.play(); //проигрывание звука окончания жизней
        Dialog dialogLives = new Dialog(this);
        dialogLives.requestWindowFeature(Window.FEATURE_NO_TITLE); //диалог без названия
        dialogLives.setContentView(R.layout.lives_dialog); //выбор макета для диалога

        Window w = dialogLives.getWindow();
        w.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION //Скрываем нижнюю панель навигации.
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY); //Появляется поверх игры и исчезает.

        dialogLives.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //затемнение заднего фона диалога
        dialogLives.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT); //растягивания диалога на экране
        dialogLives.setCancelable(false); //отключение системной кнопки назад
        dialogLives.show(); //отображение диалога

        //обработка кнопки закрыть (крестик) - начало.
        MaterialButton close = dialogLives.findViewById(R.id.bt_close_dialog);
        close.setOnClickListener(v -> {
            soundLivesDialog.stopPlay();
            onBackPressed();
        });
        //обработка кнопки закрыть (крестик) - конец.


        //обработка кнопки продолжить - начало
        MaterialButton restore = dialogLives.findViewById(R.id.bt_restore);
        restore.setOnClickListener(v -> {
            soundLivesDialog.stopPlay();
            //просмотр рекламы c вознаграждением
            myRewardedAd.showRewardedAd(Level1.this, binding.tvHeartCounter);
            dialogLives.cancel();
            myRewardedAd.loadRewardedAd(this);
        });
        //обработка кнопки продолжить - конец

    }


    /**
     * Метод работы карточек
     */
    @SuppressLint("ClickableViewAccessibility")
    private void initCards() {

        animation = AnimationUtils.loadAnimation(this, R.anim.my_animation); //инициализация анимации
        slider = binding.slider; //инициализации слайдера.

        //вызов метода инициализации вьюшек макета и присваивания рандомных значений карточкам
        initCardViews();

        //обработка нажатия на левую карточку - начало
        binding.tvLeftNumber.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                //условие на касание карточки - начало
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    binding.tvRightNumber.setEnabled(false); //отключаем правую карточку на время взаимодействия с левой карточкой

                    //сравнение значений карточек - начало
                    if (leftNumCard > rightNumCard) { //если левая карточка больше - верный ответ
                        binding.tvLeftNumber.setBackground(getDrawable(R.drawable.tv_style_green_40));
                    } else { //если правая карточка больше - неверный ответ
                        binding.tvLeftNumber.setBackground(getDrawable(R.drawable.tv_style_red_40));
                    }
                    //сравнение значений карточек - конец
                }
                //условие на касание карточки - конец

                //условие на отпускание карточки - начало
                else if (event.getAction() == MotionEvent.ACTION_UP) {

                    //сравнение значений карточек - начало
                    if (leftNumCard > rightNumCard) { //если левая карточка больше - верный ответ

                        //начисление очка за правильный ответ - начало
                        if (slider.getValue() < slider.getValueTo()) {
                            slider.setValue(slider.getValue() + trueAnswerPoint);
                        }
                        //начисление очка за правильный ответ - конец

                    } else { //если правая карточка больше - неверный ответ


                        //проверка на премиум доступ - начало
                        if (!livesSingleton.isEndlessLives()) { //если бесконечных жизней нет

                            //проверка на оставшиеся жизни - начало
                            if (livesSingleton.getCurrentLives() > 0) {
                                livesSingleton.setCurrentLives(livesSingleton.getCurrentLives() - oneLive); //вычитание жизни из текущего количества жизней
                                binding.tvHeartCounter.setText(String.valueOf(livesSingleton.getCurrentLives()));

                                //условие при окончании жизней - начало
                                if (livesSingleton.getCurrentLives() == 0) {
                                    initLivesDialog(); //вызов диалога конца жизней
                                }
                                // //условие при окончании жизней - конец
                            }
                            //проверка на оставшиеся жизни - конец
                        }
                        //проверка на премиум доступ - конец


                        //отнимание поинта за неверный ответ - начало
                        if (slider.getValue() > 1) {
                            slider.setValue(slider.getValue() - wrongAnswerPoint);
                        } else if (slider.getValue() == 1) {
                            slider.setValue(0);
                        }
                        //отнимание поинта за неверный ответ - конец
                    }
                    //сравнение значений карточек - конец

                    //проверка на завершение уровня - начало
                    if (slider.getValue() == slider.getValueTo()) {//если строка состояния заполнена

                        if (!livesSingleton.isEndlessLives()) {
                            myInterstitialAd.setLevelCompleteCounter(myInterstitialAd.getLevelCompleteCounter() + 1); //наращивание счетчика завершенных уровневней для показа рекламы
                        }

                        getProgress = getSharedPreferences(SAVE_FILE, MODE_PRIVATE); //Получение данных из файла c сохранением
                        levelCounter = getProgress.getInt(LEVEL_KEY, 1); //Получение значениея пройденных уровней.

                        //Условие по счетчику пройденных уровней - начало
                        if (levelCounter <= 1) { //если кол-во пройденных уровней 1 или меньше
                            saveProgress = getProgress.edit();
                            saveProgress.putInt(LEVEL_KEY, 2); //сохраняем в файл значение 2
                            saveProgress.apply();
                        }
                        //Условие по счетчику пройденных уровней - конец

                        initEndDialog(); //вызов диалога конца уровня

                    } else {//если строка незаполнена
                        binding.tvLeftNumber.startAnimation(animation); //проигрывание анимации
                        initCardViews(); //присваиваем новые значения
                    }
                    //проверка строки прогресса игры на заполненность - конец

                    binding.tvRightNumber.setEnabled(true); //делаем правую карточку активной
                }
                //проверка на завершение уровня - конец

                return true;
            }
        });
        //обработка нажатия на левую карточку - конец


        //обработка нажатия на правую карточку - начало
        binding.tvRightNumber.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                //условие на касание карточки - начало
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    binding.tvLeftNumber.setEnabled(false); //отключаем левую карточку на время взаимодействия с левой карточкой

                    //сравнение значений карточек - начало
                    if (rightNumCard > leftNumCard) { //если правая карточка больше - верный ответ
                        binding.tvRightNumber.setBackground(getDrawable(R.drawable.tv_style_green_40));
                    } else { //если левая карточка больше - неверный ответ
                        binding.tvRightNumber.setBackground(getDrawable(R.drawable.tv_style_red_40));
                    }
                    //сравнение значений карточек - конец
                }
                //условие на касание карточки - конец

                //условие на отпускание карточки - начало
                else if (event.getAction() == MotionEvent.ACTION_UP) {

                    //сравнение значений карточек - начало
                    if (rightNumCard > leftNumCard) { //если правая карточка больше - верный ответ

                        //начисление очка за правильный ответ - начало
                        if (slider.getValue() < slider.getValueTo()) {
                            slider.setValue(slider.getValue() + trueAnswerPoint);
                        }
                        //начисление очка за правильный ответ - конец

                    } else { //если левая карточка больше - неверный ответ

                        //проверка на премиум доступ - начало
                        if (!livesSingleton.isEndlessLives()) { //если премиума нет

                            //проверка на оставшиеся жизни - начало
                            if (livesSingleton.getCurrentLives() > 0) {
                                livesSingleton.setCurrentLives(livesSingleton.getCurrentLives() - oneLive); //вычитание жизни из текущего количества жизней
                                binding.tvHeartCounter.setText(String.valueOf(livesSingleton.getCurrentLives()));

                                //условие при окончании жизней - начало
                                if (livesSingleton.getCurrentLives() == 0) {
                                    initLivesDialog(); //вызов диалога конца жизней
                                }
                                //условие при окончании жизней - конец
                            }
                            //проверка на оставшиеся жизни - конец
                        }
                        //проверка на премиум доступ - конец

                        //отнимание поинта за неверный ответ - начало
                        if (slider.getValue() > 1) {
                            slider.setValue(slider.getValue() - wrongAnswerPoint);
                        } else if (slider.getValue() == 1) {
                            slider.setValue(0);
                        }
                        //отнимание жизни за неверный ответ - конец
                    }
                    //сравнение значений карточек - конец

                    //проверка на завершение уровня - начало
                    if (slider.getValue() == slider.getValueTo()) {//если строка состояния заполнена

                        if (!livesSingleton.isEndlessLives()) {
                            myInterstitialAd.setLevelCompleteCounter(myInterstitialAd.getLevelCompleteCounter() + 1); //наращивание счетчика завершенных уровневней для показа рекламы
                        }

                        getProgress = getSharedPreferences(SAVE_FILE, MODE_PRIVATE); //Получение данных из файла c сохранением
                        levelCounter = getProgress.getInt(LEVEL_KEY, 1); //Получение значениея пройденных уровней.

                        //Условие по счетчику пройденных уровней - начало
                        if (levelCounter <= 1) { //если кол-во пройденных уровней 1 или меньше
                            saveProgress = getProgress.edit();
                            saveProgress.putInt(LEVEL_KEY, 2); //сохраняем в файл значение 2
                            saveProgress.apply();
                        }
                        //Условие по счетчику пройденных уровней - конец

                        initEndDialog(); //вызов диалога завершения уровня
                    } else { //если строка незаполнена
                        binding.tvRightNumber.startAnimation(animation); //проигрывание анимации
                        initCardViews(); //присваивание карточкам новых значений
                    }
                    //проверка на завершение уровня - конец

                    binding.tvLeftNumber.setEnabled(true); //активация левой карточки.
                }
                //условие на отпускание карточки - конец

                return true;
            }
        });
        //обработка нажатия на правую карточку - конец
    }


    /**
     * метода инициализации вьюшек макета
     * и присваивание рандомных значений карточкам
     */
    public void initCardViews() {

        String[] textArray = getResources().getStringArray(R.array.textArray); //создание массива на основе массива из ресурсов
        Random random = new Random();
        leftNumCard = random.nextInt(textArray.length); //рандом значение для левой карты
        rightNumCard = random.nextInt(textArray.length); //рандом значение для правой карты

        //цикл для получения разных значений - начало
        while (leftNumCard == rightNumCard) { //если значения равны - правая карта получает новое значение
            rightNumCard = random.nextInt(textArray.length);
        }
        //цикл для получения разных значений - конец

        binding.tvLeftNumber.setText(String.valueOf(leftNumCard));
        binding.tvRightNumber.setText(String.valueOf(rightNumCard));
        binding.tvLeftNumberText.setText(textArray[leftNumCard]);
        binding.tvRightNumberText.setText(textArray[rightNumCard]);
        binding.tvLeftNumber.setBackground(getDrawable(R.drawable.tv_style_white_40));
        binding.tvRightNumber.setBackground(getDrawable(R.drawable.tv_style_white_40));
    }


    /**
     * Метод системной кнопки назад
     */
    @Override
    public void onBackPressed() {
        soundEndDialog.stopPlay(); //выключение звука в конце уровня
        soundLivesDialog.stopPlay(); //выключение звука конца жизней
        Intent intent = new Intent(Level1.this, GameLevels.class);
        startActivity(intent); //переход к активити выбора уровней
        finish(); //уничтожение активити
    }


    /**
     * Метод уничтожения активити
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();

        //выключение звука в конце уровня
        if (soundEndDialog.isPlaying()) {
            soundEndDialog.stopPlay();
        }

        //выключение звука конца жизней
        if (soundLivesDialog.isPlaying()) {
            soundLivesDialog.stopPlay();
        }
    }

}