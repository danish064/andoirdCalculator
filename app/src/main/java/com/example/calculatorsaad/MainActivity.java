package com.example.calculatorsaad;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Creating references to the UI elements
        TextView tvsec = findViewById(R.id.idTVSecondary);
        TextView tvMain = findViewById(R.id.idTVprimary);
        Button bac = findViewById(R.id.bac);
        Button bc = findViewById(R.id.bc);
        Button bbrac1 = findViewById(R.id.bbrac1);
        Button bbrac2 = findViewById(R.id.bbrac2);
        Button bsin = findViewById(R.id.bsin);
        Button bcos = findViewById(R.id.bcos);
        Button btan = findViewById(R.id.btan);
        Button blog = findViewById(R.id.blog);
        Button bln = findViewById(R.id.bln);
        Button bfact = findViewById(R.id.bfact);
        Button bsquare = findViewById(R.id.bsquare);
        Button bsqrt = findViewById(R.id.bsqrt);
        Button binv = findViewById(R.id.binv);
        Button b0 = findViewById(R.id.b0);
        Button b9 = findViewById(R.id.b9);
        Button b8 = findViewById(R.id.b8);
        Button b7 = findViewById(R.id.b7);
        Button b6 = findViewById(R.id.b6);
        Button b5 = findViewById(R.id.b5);
        Button b4 = findViewById(R.id.b4);
        Button b3 = findViewById(R.id.b3);
        Button b2 = findViewById(R.id.b2);
        Button b1 = findViewById(R.id.b1);
        Button bpi = findViewById(R.id.bpi);
        Button bmul = findViewById(R.id.bmul);
        Button bminus = findViewById(R.id.bminus);
        Button bplus = findViewById(R.id.bplus);
        Button bdiv = findViewById(R.id.bdiv);

        Button bequal = findViewById(R.id.bequal);
        Button bdot = findViewById(R.id.bdot);


        // Setting on click listeners to all buttons

        bequal.setOnClickListener(view -> {
            String mainExpression = (String) tvMain.getText();
            double result = eval(mainExpression);

            tvsec.setText(Double.toString(result));
        });
        bac.setOnClickListener(view -> {
            tvMain.setText("");
            tvsec.setText("");
        });
        bdot.setOnClickListener(view -> tvMain.setText("."));
        bc.setOnClickListener(view -> {
            String a = (String) tvMain.getText();
            tvMain.setText(a.substring(0, a.length() - 1));
        });
        bbrac1.setOnClickListener(view -> tvMain.setText(tvMain.getText() + "("));
        bbrac2.setOnClickListener(view -> tvMain.setText(tvMain.getText() + ")"));
        b1.setOnClickListener(view -> tvMain.setText(tvMain.getText() + "1"));
        b2.setOnClickListener(view -> tvMain.setText(tvMain.getText() + "2"));
        b3.setOnClickListener(view -> tvMain.setText(tvMain.getText() + "3"));
        b4.setOnClickListener(view -> tvMain.setText(tvMain.getText() + "4"));
        b5.setOnClickListener(view -> tvMain.setText(tvMain.getText() + "5"));
        b6.setOnClickListener(view -> tvMain.setText(tvMain.getText() + "6"));
        b7.setOnClickListener(view -> tvMain.setText(tvMain.getText() + "7"));
        b8.setOnClickListener(view -> tvMain.setText(tvMain.getText() + "8"));
        b9.setOnClickListener(view -> tvMain.setText(tvMain.getText() + "9"));
        b0.setOnClickListener(view -> tvMain.setText(tvMain.getText() + "0"));

        bmul.setOnClickListener(view -> tvMain.setText(tvMain.getText() + "*"));
        bminus.setOnClickListener(view -> tvMain.setText(tvMain.getText() + "-"));
        bplus.setOnClickListener(view -> tvMain.setText(tvMain.getText() + "+"));
        bdiv.setOnClickListener(view -> tvMain.setText(tvMain.getText() + "/"));
    }

    public static double eval(final String str) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char) ch);
                return x;
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)` | number
            //        | functionName `(` expression `)` | functionName factor
            //        | factor `^` factor

            double parseExpression() {
                double x = parseTerm();
                for (; ; ) {
                    if (eat('+')) x += parseTerm(); // addition
                    else if (eat('-')) x -= parseTerm(); // subtraction
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (; ; ) {
                    if (eat('*')) x *= parseFactor(); // multiplication
                    else if (eat('/')) x /= parseFactor(); // division
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return +parseFactor(); // unary plus
                if (eat('-')) return -parseFactor(); // unary minus

                double x;
                int startPos = this.pos;
                if (eat('(')) { // parentheses
                    x = parseExpression();
                    if (!eat(')')) throw new RuntimeException("Missing ')'");
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else if (ch >= 'a' && ch <= 'z') { // functions
                    while (ch >= 'a' && ch <= 'z') nextChar();
                    String func = str.substring(startPos, this.pos);
                    if (eat('(')) {
                        x = parseExpression();
                        if (!eat(')'))
                            throw new RuntimeException("Missing ')' after argument to " + func);
                    } else {
                        x = parseFactor();
                    }
                    switch (func) {
                        case "sqrt":
                            x = Math.sqrt(x);
                            break;
                        case "sin":
                            x = Math.sin(Math.toRadians(x));
                            break;
                        case "cos":
                            x = Math.cos(Math.toRadians(x));
                            break;
                        case "tan":
                            x = Math.tan(Math.toRadians(x));
                            break;
                        default:
                            throw new RuntimeException("Unknown function: " + func);
                    }
                } else {
                    throw new RuntimeException("Unexpected: " + (char) ch);
                }

                if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation

                return x;
            }
        }.parse();
    }
}