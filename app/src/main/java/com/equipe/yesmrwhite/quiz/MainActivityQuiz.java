package com.equipe.yesmrwhite.quiz;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.equipe.yesmrwhite.main.MainActivity;
import com.equipe.yesmrwhite.quimica.Elemento;
import com.equipe.yesmrwhite.quimica.TabelaPeriodica;
import com.example.syllas.yesmrwhite.R;

public class MainActivityQuiz extends Activity
{
    // String used when logging error messages
    private static final String TAG = "QuizGame Activity";

    private List<Elemento> elementos;
    private TabelaPeriodica tabela;
    private  List<Elemento> elementosEscolhidos;
    private  List<Elemento> elementosJogada;
    private Map<String, Boolean> elementsMap;
    Elemento nextElement;
    int perguntaRandom;
    private String correctAnswer;
    private int totalGuesses;
    private int correctAnswers;
    private int guessRows;
    private Random random;
    private Handler handler;
    private Animation shakeAnimation;

    private TextView answerTextView;
    private TextView questionNumberTextView;
    private TextView perguntaTextView;
    private ImageView flagImageView;
    private TableLayout buttonTableLayout;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        tabela = new TabelaPeriodica(getApplicationContext());
        elementos = tabela.getTabela(); //Recupera toda a tabela periodica
        elementosEscolhidos = new ArrayList<Elemento>();
        elementosJogada = new ArrayList<Elemento>();
        elementsMap = new HashMap<String, Boolean>();
        guessRows = 1; // 1 linha de escolhas padrao
        random = new Random(); // gerador de num aleatorio
        handler = new Handler();

        // carrega a animação da resp incorreta
        shakeAnimation =
                AnimationUtils.loadAnimation(this, R.anim.incorrect_shake);
        shakeAnimation.setRepeatCount(3); //repete 3 vezes



        String[] classeNames =
                getResources().getStringArray(R.array.elementsList);


        for (String classe : classeNames )
            elementsMap.put(classe, true);


        // get references to GUI components
        questionNumberTextView =
                (TextView) findViewById(R.id.questionNumberTextView);
        flagImageView = (ImageView) findViewById(R.id.flagImageView);
        buttonTableLayout =
                (TableLayout) findViewById(R.id.buttonTableLayout);
        answerTextView = (TextView) findViewById(R.id.answerTextView);

        // set questionNumberTextView's text
        questionNumberTextView.setText(
                getResources().getString(R.string.question) + " 1 " +
                        getResources().getString(R.string.of) + " 10");


        //resetQuiz(); // start a new quiz

        myResetQuiz(); // start a new quiz

    } // end method onCreate


    //Chama configurações
    public void config(View view){
        MainActivityQuiz.this.openOptionsMenu();
    }


    // set up and start the next quiz MY reset
    private void myResetQuiz()
    {
        // use the AssetManager to get the image flag
        // file names for only the enabled regions
        AssetManager assets = getAssets(); // get the app's AssetManager
        elementosEscolhidos.clear();
        elementosJogada.clear();


        Set<String> classes = elementsMap.keySet(); // get Set of regions

        // loob para todas as classes
        for (String classe : classes)
        {
            if (elementsMap.get(classe)) // if classe marcada
            {
                //Adiciona todos os elementos da classe marcada.

                for(int i=0; i< tabela.getElementByClassTipo(classe).size(); i++){
                    elementosEscolhidos.add(tabela.getElementByClassTipo(classe).get(i));
                }

            } // end if
        } // end for


        correctAnswers = 0; // reseta o numero de resp correta
        totalGuesses = 0; // reseta o numero de opçoes

        // adiciona 10 elementos aleatorios no elementosEscolhidos
        int elementCounter = 1;
        int numberOfElements = elementosEscolhidos.size();

        //Se não tiver elementos suficiente reseta e bota todos os elementos
        //GAMBIARRA - Mudar depois.
        if (numberOfElements < 10){
            elementosEscolhidos.clear();

            String[] classeNames =
                    getResources().getStringArray(R.array.elementsList);

            for (String classe : classeNames )
                elementsMap.put(classe, true);


            // loob para todas as classes
            for (String classe : classes)
            {
                System.out.println("Calsse: ".concat(classe));
                if (elementsMap.get(classe)) // if classe marcada
                {
                    //Adiciona todos os elementos da classe marcada.

                    for(int i=0; i< tabela.getElementByClassTipo(classe).size(); i++){
                        elementosEscolhidos.add(tabela.getElementByClassTipo(classe).get(i));
                    }

                } // end if
            } // end for
            numberOfElements = elementosEscolhidos.size();
        }
        //FIM - GAMBIARRA

        while (elementCounter <= 10)
        {
            int randomIndex = random.nextInt(numberOfElements); // random index

            // pega um elemento aleatorio
            Elemento elementRandom = elementosEscolhidos.get(randomIndex);

            // if the region is enabled and it hasn't already been chosen
            if (!elementosJogada.contains(elementRandom))
            {
                elementosJogada.add(elementRandom); // add the file to the list
                ++elementCounter;
            } // end if
        } // end while

        loadNextElement(); // carrega o primeiro elemento
    } // My resetQuiz



    // correga o proximo elemento
    private void loadNextElement()
    {
        // pega o proxima elemento e tira ele da lista
        nextElement = elementosJogada.remove(0);

        perguntaRandom = random.nextInt(3);
        switch (perguntaRandom){
            case 0:
                correctAnswer = nextElement.getNome();
                perguntaTextView = (TextView) findViewById(R.id.guessCountryTextView);
                perguntaTextView.setText(R.string.pergunta1);
                break;
            case 1:
                correctAnswer = nextElement.getFamilia();
                perguntaTextView = (TextView) findViewById(R.id.guessCountryTextView);
                perguntaTextView.setText(R.string.pergunta2);
                break;
            case 2:
                correctAnswer = nextElement.getSubTipo();
                perguntaTextView = (TextView) findViewById(R.id.guessCountryTextView);
                perguntaTextView.setText(R.string.pergunta3);
                break;
        }



        answerTextView.setText(""); // limpa answerTextView

        // mostra o numero da questão no quiz
        questionNumberTextView.setText(
                getResources().getString(R.string.question) + " " +
                        (correctAnswers + 1) + " " +
                        getResources().getString(R.string.of) + " 10");


        // use AssetManager to load next image from assets folder
        AssetManager assets = getAssets(); // get app's AssetManager
        InputStream stream; // used to read in flag images

        try
        {
            // get an InputStream to the asset representing the next flag
            stream = assets.open(nextElement.getImagem());

            // load the asset as a Drawable and display on the flagImageView
            Drawable elementImg = Drawable.createFromStream(stream,nextElement.getNome());
            flagImageView.setImageDrawable(elementImg);
        } // end try
        catch (IOException e)
        {
            Log.e(TAG, "Error loading " + nextElement.getNome(), e);
        } // end catch

        // clear prior answer Buttons from TableRows
        for (int row = 0; row < buttonTableLayout.getChildCount(); ++row)
            ((TableRow) buttonTableLayout.getChildAt(row)).removeAllViews();

        Collections.shuffle(elementos); // bagunça a lista

        // bota a resposta correta pro final da lista (Interessante para não repetir nome.)
        int correct = elementos.indexOf(nextElement);
        elementos.add(elementos.remove(correct));

        // get a reference to the LayoutInflater service
        LayoutInflater inflater = (LayoutInflater) getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);

        List<String> respostas = new ArrayList<String>();
        // Adiciona 3, 6, ou 9 botoes de resposta de acordo com guessRows
        for (int row = 0; row < guessRows; row++)
        {
            TableRow currentTableRow = getTableRow(row);

            // place Buttons in currentTableRow
            for (int column = 0; column < 3; column++)
            {
                // inflate guess_button.xml to create new Button
                Button newGuessButton =
                        (Button) inflater.inflate(R.layout.guess_button, null);

                // Seta as alternativas de modo a não repetir
                boolean control;
                int cont;
                switch (perguntaRandom){
                    case 0:
                         cont = (row * 3) + column;
                        do{
                            if(respostas.contains(elementos.get(cont).getNome())){
                                control = true;
                                cont++;
                            }else{
                                respostas.add(elementos.get(cont).getNome());
                                newGuessButton.setText(elementos.get(cont).getNome());
                                control=false;
                            }
                        }while(control && cont < (elementos.size()-2));
                        break;
                    case 1:
                        cont = (row * 3) + column;
                        do{
                            if(respostas.contains(elementos.get(cont).getFamilia())
                                    || elementos.get(cont).getFamilia().equals(nextElement.getFamilia())){
                                control = true;
                                cont++;
                            }else{
                                respostas.add(elementos.get(cont).getFamilia());
                                newGuessButton.setText(elementos.get(cont).getFamilia());
                                control=false;
                            }

                        }while(control && cont < (elementos.size()-2));
                        break;
                    case 2:
                        cont = (row * 3) + column;
                        do{
                            if(respostas.contains(elementos.get(cont).getSubTipo())
                                    || elementos.get(cont).getSubTipo().equals(nextElement.getSubTipo())){
                                control = true;
                                cont++;
                            }else{
                                respostas.add(elementos.get(cont).getSubTipo());
                                newGuessButton.setText(elementos.get(cont).getSubTipo());
                                control=false;
                            }

                        }while(control && cont < (elementos.size()-2));
                        break;
                }


                // register answerButtonListener to respond to button clicks
                newGuessButton.setOnClickListener(guessButtonListener);
                currentTableRow.addView(newGuessButton);
            } // end for
        } // end for

        // randomly replace one Button with the correct answer
        int row = random.nextInt(guessRows); // pick random row
        int column = random.nextInt(3); // pick random column
        TableRow randomTableRow = getTableRow(row); // get the TableRow
        switch (perguntaRandom){
            case 0:
                ((Button)randomTableRow.getChildAt(column)).setText(nextElement.getNome());
                break;
            case 1:
                ((Button)randomTableRow.getChildAt(column)).setText(nextElement.getFamilia());
                break;
            case 2:
                ((Button)randomTableRow.getChildAt(column)).setText(nextElement.getSubTipo());
                break;
        }
    } // end method loadNextElement



    // returns the specified TableRow
    private TableRow getTableRow(int row)
    {
        return (TableRow) buttonTableLayout.getChildAt(row);
    } // end method getTableRow

    // called when the user selects an answer
    private void submitGuess(Button guessButton)
    {
        String guess = guessButton.getText().toString();
        String answer = "";
        switch (perguntaRandom){
            case 0:
                answer = nextElement.getNome();
                break;
            case 1:
                answer = nextElement.getFamilia();
                break;
            case 2:
                answer = nextElement.getSubTipo();
                break;
        }
        ++totalGuesses; // increment the number of guesses the user has made

        // if the guess is correct
        if (guess.equals(answer))
        {
            ++correctAnswers; // increment the number of correct answers

            // display "Correct!" in green text
            answerTextView.setText(answer + "!");
            answerTextView.setTextColor(
                    getResources().getColor(R.color.correct_answer));

            disableButtons(); // disable all answer Buttons

            // if the user has correctly identified 10 flags
            if (correctAnswers == 10)
            {
                // create a new AlertDialog Builder
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setTitle(R.string.reset_quiz); // title bar string

                // set the AlertDialog's message to display game results
                builder.setMessage(String.format("%d %s, %.02f%% %s",
                        totalGuesses, getResources().getString(R.string.guesses),
                        (1000 / (double) totalGuesses),
                        getResources().getString(R.string.correct)));

                builder.setCancelable(false);
                // add "Reset Quiz" Button
                builder.setNeutralButton("Voltar",new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int id)
                            {
                                startActivity(new Intent(getBaseContext(), MainActivity.class));
                            } // end method onClick
                        }
                ); // end anonymous inner class);
                builder.setPositiveButton(R.string.reset_quiz,
                        new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int id)
                            {
                                myResetQuiz();
                            } // end method onClick
                        } // end anonymous inner class
                ); // end call to setPositiveButton

                // create AlertDialog from the Builder
                AlertDialog resetDialog = builder.create();
                resetDialog.show(); // display the Dialog
            } // end if
            else // answer is correct but quiz is not over
            {
                // load the next flag after a 1-second delay
                handler.postDelayed(
                        new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                loadNextElement();
                            }
                        }, 1000); // 1000 milliseconds for 1-second delay
            } // end else
        } // end if
        else // guess was incorrect
        {
            // play the animation
            flagImageView.startAnimation(shakeAnimation);

            // display "Incorrect!" in red
            answerTextView.setText(R.string.incorrect_answer);
            answerTextView.setTextColor(
                    getResources().getColor(R.color.incorrect_answer));
            guessButton.setEnabled(false); // disable the incorrect answer
        } // end else
    } // end method submitGuess

    // utility method that disables all answer Buttons
    private void disableButtons()
    {
        for (int row = 0; row < buttonTableLayout.getChildCount(); ++row)
        {
            TableRow tableRow = (TableRow) buttonTableLayout.getChildAt(row);
            for (int i = 0; i < tableRow.getChildCount(); ++i)
                tableRow.getChildAt(i).setEnabled(false);
        } // end outer for
    } // end method disableButtons

    // create constants for each menu id
    private final int CHOICES_MENU_ID = Menu.FIRST;
    private final int REGIONS_MENU_ID = Menu.FIRST + 1;
    private final int SAIR_MENU_ID = Menu.FIRST + 2;

    // called when the user accesses the options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);

        // add two options to the menu - "Choices" and "Regions"
        menu.add(Menu.NONE, CHOICES_MENU_ID, Menu.NONE, R.string.choices);
        menu.add(Menu.NONE, REGIONS_MENU_ID, Menu.NONE, R.string.regions);
        menu.add(Menu.NONE, SAIR_MENU_ID, Menu.NONE, R.string.sair);

        return true; // display the menu
    }  // end method onCreateOptionsMenu

    // called when the user selects an option from the menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // switch the menu id of the user-selected option
        switch (item.getItemId())
        {
            case CHOICES_MENU_ID:
                // create a list of the possible numbers of answer choices
                final String[] possibleChoices =
                        getResources().getStringArray(R.array.guessesList);

                // create a new AlertDialog Builder and set its title
                AlertDialog.Builder choicesBuilder =
                        new AlertDialog.Builder(this);
                choicesBuilder.setTitle(R.string.choices);

                // add possibleChoices's items to the Dialog and set the
                // behavior when one of the items is clicked
                choicesBuilder.setItems(R.array.guessesList,
                        new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int item)
                            {
                                // update guessRows to match the user's choice
                                guessRows = Integer.parseInt(
                                        possibleChoices[item].toString()) / 3;
                                myResetQuiz(); // reset the quiz
                            } // end method onClick
                        } // end anonymous inner class
                );  // end call to setItems

                // create an AlertDialog from the Builder
                AlertDialog choicesDialog = choicesBuilder.create();
                choicesDialog.show(); // show the Dialog
                return true;

            case REGIONS_MENU_ID:
                // get array of world regions
                final String[] regionNames =
                        elementsMap.keySet().toArray(new String[elementsMap.size()]);

                // boolean array representing whether each region is enabled
                boolean[] regionsEnabled = new boolean[elementsMap.size()];
                for (int i = 0; i < regionsEnabled.length; ++i)
                    regionsEnabled[i] = elementsMap.get(regionNames[i]);

                // create an AlertDialog Builder and set the dialog's title
                AlertDialog.Builder regionsBuilder =
                        new AlertDialog.Builder(this);
                regionsBuilder.setTitle(R.string.regions);

                // replace _ with space in region names for display purposes
                String[] displayNames = new String[regionNames.length];
                for (int i = 0; i < regionNames.length; ++i)
                    displayNames[i] = regionNames[i].replace('_', ' ');

                // add displayNames to the Dialog and set the behavior
                // when one of the items is clicked
                regionsBuilder.setMultiChoiceItems(
                        displayNames, regionsEnabled,
                        new DialogInterface.OnMultiChoiceClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which,
                                                boolean isChecked)
                            {
                                // include or exclude the clicked region
                                // depending on whether or not it's checked
                                elementsMap.put(
                                        regionNames[which].toString(), isChecked);
                            } // end method onClick
                        } // end anonymous inner class
                ); // end call to setMultiChoiceItems

                // resets quiz when user presses the "Reset Quiz" Button
                regionsBuilder.setPositiveButton(R.string.reset_quiz,
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int button)
                            {
                                myResetQuiz(); // reset the quiz
                            } // end method onClick
                        } // end anonymous inner class
                ); // end call to method setPositiveButton

                // create a dialog from the Builder
                AlertDialog regionsDialog = regionsBuilder.create();
                regionsDialog.show(); // display the Dialog
                return true;

            case SAIR_MENU_ID:
                //Cod. Sair aqui (VOLTAR PRA ACTIVITY PRINCIPAL)
                startActivity(new Intent(this, MainActivity.class));
                this.finishAfterTransition();
                Log.i(TAG, "onOptionsItemSelected: saiuuuuuuu");
        } // end switch

        return super.onOptionsItemSelected(item);
    } // end method onOptionsItemSelected

    // called when a guess Button is touched
    private OnClickListener guessButtonListener = new OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            submitGuess((Button) v); // pass selected Button to submitGuess
        } // end method onClick
    }; // end answerButtonListener
} // end FlagQuizGame