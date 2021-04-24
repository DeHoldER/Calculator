package ru.geekbrains.calculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private final static String keyCalculatorState = "CalculatorState";

    private CalculatorHandler calculator;

    private TextView mOutputScreen;
    private TextView mActionChar;

    private final int[] numberButtonIds = new int[]{R.id.button0,
            R.id.button1, R.id.button2, R.id.button3,
            R.id.button4, R.id.button5, R.id.button6,
            R.id.button7, R.id.button8, R.id.button9};

    private Button mButtonPt;

    private Button mButtonCancel;
    private Button mButtonPlus;
    private Button mButtonMinus;
    private Button mButtonMultiply;
    private Button mButtonDivide;
    private Button mButtonEquals;

    @Override
    public void onSaveInstanceState(@NonNull Bundle instanceState) {
        super.onSaveInstanceState(instanceState);
        instanceState.putParcelable(keyCalculatorState, calculator);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle instanceState) {
        super.onRestoreInstanceState(instanceState);
        calculator = instanceState.getParcelable(keyCalculatorState);

        if (calculator.getAction() != '0') {
            mActionChar.setText(String.valueOf(calculator.getAction()));
        }
        if (calculator.stringBuilder.length() != 0) {
            printNumOnScreen();
        } else printNumOnScreen(calculator.getStringResult());
    }

    void aplog() {
//        System.out.println("aplog (sb length): " + calculator.stringBuilder.length());
//        System.out.println("aplog (sb): " + calculator.stringBuilder.toString());
//        System.out.println("aplog (firstNum): " + calculator.getFirstNumber());
        System.out.println("aplog (action): " + calculator.getAction());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calculator = new CalculatorHandler();

        initView();
        initButtonListeners();
    }

    private void initView() {
        mOutputScreen = findViewById(R.id.output_screen);
        mActionChar = findViewById(R.id.action_char);

        mButtonPt = findViewById(R.id.buttonPt);

        mButtonCancel = findViewById(R.id.button_cancel);
        mButtonPlus = findViewById(R.id.button_plus);
        mButtonMinus = findViewById(R.id.button_minus);
        mButtonMultiply = findViewById(R.id.button_multiply);
        mButtonDivide = findViewById(R.id.button_divide);
        mButtonEquals = findViewById(R.id.button_equals);
    }

    private void initButtonListeners() {
        setNumberButtonListeners();

        addFuncButtonListener(mButtonPt);
        addFuncButtonListener(mButtonPlus);
        addFuncButtonListener(mButtonMinus);
        addFuncButtonListener(mButtonMultiply);
        addFuncButtonListener(mButtonDivide);

        addFuncButtonListener(mButtonEquals);

        mButtonCancel.setOnClickListener(v -> {
            calculator.totalReset();
            mOutputScreen.setText("0");
            mActionChar.setText("");
        });
    }

    private void setNumberButtonListeners() {
        for (int i = 0; i < numberButtonIds.length; i++) {
            int index = i;
            findViewById(numberButtonIds[i]).setOnClickListener(v -> {

                if (!(calculator.stringBuilder.length() == 0 && index == 0)) {
                    calculator.stringBuilder.append(String.valueOf(index));
                    printNumOnScreen();
                }
            });
        }
    }

    // СЛУШАТЕЛЬ ФУНКЦИОНАЛЬНЫХ КНОПОК
    private void addFuncButtonListener(Button button) {
        button.setOnClickListener(v -> onFuncButtonClick(button));
    }

    private void onFuncButtonClick(Button button) {
        if (button == mButtonPt) {
            onButtonPointClick();
        }

        // Если нажаты не кнопки равно и не точка
        if (button != mButtonEquals && button != mButtonPt) {
            if (calculator.stringBuilder.length() == 0) {
                changeAction(button);
            } else {
                try {
                    calculator.addCurrentNumber();
                } catch (Exception e) {
                    printNumOnScreen("Error!");
                }
                changeAction(button);
                printNumOnScreen(calculator.getStringResult());
            }
        }
        if (button == mButtonEquals) {
            if (calculator.getAction() != '0') {
                try {
                    calculator.addCurrentNumber();
                } catch (Exception e) {
                    printNumOnScreen("Error!");
                }
                clearAction();
                printNumOnScreen(calculator.getStringResult());
            }
        }
    }

    private void clearAction() {
        mActionChar.setText("");
        calculator.setAction('0');
    }

    private void changeAction(Button button) {
        mActionChar.setText(button.getText().toString());
        calculator.setAction(button.getText().toString().charAt(0));
    }

    private void printNumOnScreen() {
        mOutputScreen.setText(calculator.stringBuilder);
    }

    private void printNumOnScreen(String s) {
        mOutputScreen.setText(s);
    }

    public void onButtonPointClick() {
        calculator.setFractionalNumber(true);
        if (calculator.stringBuilder.length() == 0) {
            mOutputScreen.setText("0");
            calculator.stringBuilder.append(0);
        }

        if (!calculator.isCurrentFractionalNumber()) {
            mOutputScreen.setText(getResources().getString(R.string.point_template, mOutputScreen.getText()));
            calculator.stringBuilder.append(".");
            calculator.setCurrentFractionalNumber(true);
        }
    }

}