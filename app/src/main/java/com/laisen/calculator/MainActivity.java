package com.laisen.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.EmptyStackException;
import java.util.Scanner;
import java.util.Stack;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public EditText txtResult;
    public TextView txtInfix;
    public TextView txtPostfix;

    public Button btnOne;
    public Button btnTwo;
    public Button btnThree;
    public Button btnFor;
    public Button btnFive;
    public Button btnSix;
    public Button btnSeven;
    public Button btnEight;
    public Button btnNine;
    public Button btnZero;
    public Button btnDivide;
    public Button btnMultiply;
    public Button btnAdd;
    public Button btnSubtract;
    public Button btnLeftParenthesis;
    public Button btnRightParenthesis;
    public Button btnEquals;
    public Button btnClear;

    public boolean operated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtResult = findViewById(R.id.txt_result);
        txtInfix = findViewById(R.id.txt_infix);
        txtPostfix = findViewById(R.id.txt_postfix);
        btnOne = findViewById(R.id.btn_one);
        btnTwo = findViewById(R.id.btn_two);
        btnThree = findViewById(R.id.btn_three);
        btnFor = findViewById(R.id.btn_four);
        btnFive = findViewById(R.id.btn_five);
        btnSix = findViewById(R.id.btn_six);
        btnSeven = findViewById(R.id.btn_seven);
        btnEight = findViewById(R.id.btn_eight);
        btnNine = findViewById(R.id.btn_nine);
        btnZero = findViewById(R.id.btn_zero);
        btnDivide = findViewById(R.id.btn_divide);
        btnMultiply = findViewById(R.id.btn_multiply);
        btnAdd = findViewById(R.id.btn_add);
        btnSubtract = findViewById(R.id.btn_subtracts);
        btnLeftParenthesis = findViewById(R.id.btn_leftparenthesis);
        btnRightParenthesis = findViewById(R.id.btn_rightparenthesis);
        btnEquals = findViewById(R.id.btn_equals);
        btnClear = findViewById(R.id.btn_clear);

        btnOne.setOnClickListener(this);
        btnTwo.setOnClickListener(this);
        btnThree.setOnClickListener(this);
        btnFor.setOnClickListener(this);
        btnFive.setOnClickListener(this);
        btnSix.setOnClickListener(this);
        btnSeven.setOnClickListener(this);
        btnEight.setOnClickListener(this);
        btnNine.setOnClickListener(this);
        btnZero.setOnClickListener(this);
        btnDivide.setOnClickListener(this);
        btnMultiply.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        btnSubtract.setOnClickListener(this);
        btnLeftParenthesis.setOnClickListener(this);
        btnRightParenthesis.setOnClickListener(this);

        btnEquals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (operated) {
                    String result;
                    String infix;
                    String postfix;

                    result = txtResult.getText().toString();
                    infix = txtInfix.getText().toString();
                    postfix = txtPostfix.getText().toString();

                    txtResult.setText(result);
                    txtInfix.setText(infix);
                    txtPostfix.setText(postfix);

                } else {
                    String expression = txtResult.getText().toString();
                    if (expression != null && !expression.isEmpty()) {
                        String infixExpression = infixPostfix(expression);

                        if (!infixExpression.isEmpty()) {
                            txtInfix.setText("Expresión infija: " + expression);
                            txtPostfix.setText("Expresión postfija: " + infixExpression);

                            postfixEvaluate(infixExpression);
                        } else {
                            txtResult.setText("Error en la expresión");
                            operated = true;
                        }
                    }

                }
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (operated) {
            clear();
        }
        String text = txtResult.getText().toString();
        String value = v.getTag().toString();

        if (text == null || text.isEmpty()) {
            if (!value.equalsIgnoreCase("(") && !isNumeric(value)) {
                return;
            }
        }
        txtResult.append(v.getTag().toString());
    }

    private static String infixPostfix(String value) {
        String expression = depurar(value);
        String[] arrayInfix = expression.split(" ");

        Stack<String> inputStack = new Stack<>(); //Input
        Stack<String> auxStack = new Stack<>(); //Aux for operators
        Stack<String> outputStack = new Stack<>(); //Output

        //Add array to input stack
        for (int i = arrayInfix.length - 1; i >= 0; i--) {
            inputStack.push(arrayInfix[i]);
        }

        try {
            while (!inputStack.isEmpty()) {
                switch (hierarchy(inputStack.peek())) {
                    case 1:
                        auxStack.push(inputStack.pop());
                        break;
                    case 3:
                    case 4:
                        while (hierarchy(auxStack.peek()) >= hierarchy(inputStack.peek())) {
                            outputStack.push(auxStack.pop());
                        }
                        auxStack.push(inputStack.pop());
                        break;
                    case 2:
                        while (!auxStack.peek().equals("(")) {
                            outputStack.push(auxStack.pop());
                        }
                        auxStack.pop();
                        inputStack.pop();
                        break;
                    default:
                        outputStack.push(inputStack.pop());
                }
            }

            //Replacement blank spaces and another symbols
            String infix = expression.replace(" ", "");
            String postfix = outputStack.toString().replaceAll("[\\]\\[,]", "");

            System.out.println("Infix expression: " + infix);
            System.out.println("Postfix expression: " + postfix);

            return postfix;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return "";
    }

    private void postfixEvaluate(String exp) {
        try {
            Stack<Integer> s = new Stack<>();
            Scanner tokens = new Scanner(exp);

            while (tokens.hasNext()) {
                if (tokens.hasNextInt()) {
                    s.push(tokens.nextInt());
                } else {
                    int num2 = s.pop();
                    int num1 = s.pop();
                    String op = tokens.next();

                    switch (op) {
                        case "+":
                            s.push(num1 + num2);
                            break;
                        case "-":
                            s.push(num1 - num2);
                            break;
                        case "*":
                            s.push(num1 * num2);
                            break;
                        default:
                            s.push(num1 / num2);
                            break;
                    }
                }
            }

            txtResult.setText(String.valueOf(s.pop()));
        } catch (EmptyStackException e) {
            txtResult.setText("Error en la expresión");
        } finally {
            operated = true;
        }
    }

    public static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }

    private static String depurar(String s) {
        s = s.replaceAll("\\s+", "");
        s = "(" + s + ")";
        String symbols = "+-*/()";
        StringBuilder str = new StringBuilder();

        //Let spaces into operators
        for (int i = 0; i < s.length(); i++) {
            if (symbols.contains("" + s.charAt(i))) {
                str.append(" ").append(s.charAt(i)).append(" ");
            } else {
                str.append(s.charAt(i));
            }
        }
        return str.toString().replaceAll("\\s+", " ").trim();
    }

    private static int hierarchy(String operator) {
        int prf = 99;
        if (operator.equals("^")) prf = 5;
        if (operator.equals("*") || operator.equals("/")) prf = 4;
        if (operator.equals("+") || operator.equals("-")) prf = 3;
        if (operator.equals(")")) prf = 2;
        if (operator.equals("(")) prf = 1;
        return prf;
    }

    private void clear() {
        operated = false;
        txtResult.setText("");
        txtInfix.setText("");
        txtPostfix.setText("");
    }
}
