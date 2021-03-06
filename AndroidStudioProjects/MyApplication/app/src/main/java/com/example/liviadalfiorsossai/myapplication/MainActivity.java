package com.example.liviadalfiorsossai.myapplication;

import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {
public int jogada = 0;
public int peca_virada1;
public int peca_virada2;
public int peca_virada1_pos=-1;
public int peca_virada2_pos=-1;
public int pares_virados = 0;

public int NUM_PARES = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Será usado futuramente, para fazer animações
       // ImageView anim = (ImageView) findViewById(R.id.animacao);
       // anim.setBackgroundResource(R.drawable.animacao);

        //AnimationDrawable animation = (AnimationDrawable) anim.getBackground();
        //animation.start();


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView textview = (TextView) findViewById(R.id.textView);

        final TratadorPecas pec = new TratadorPecas(0);

        final TratadorJogada TJ = new TratadorJogada();

        final GridView gridview = (GridView) findViewById(R.id.GV);

        final ImageAdapter ImgAdptr = new ImageAdapter(this);

        final Button button2 = (Button) findViewById(R.id.button2); // Botão que ativa a exibição dos níveis do jogo




        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(MainActivity.this, button2); // Popup que exibe os níveis do jogo e é acionado por botão 2
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

                // Cadastrar callback dos itens da lista da popup
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getTitle().equals("Small Board (3x4)")){
                            NUM_PARES = 6;

                        }
                        else if(item.getTitle().equals("Medium Board (5x4)")){
                            NUM_PARES = 10;


                        }
                        else if(item.getTitle().equals("Big Board (7x4)")){
                            NUM_PARES = 14;

                         }
                        else if(item.getTitle().equals("Teste (2x4)")){
                            NUM_PARES = 4;

                       }
                        pec.re_inicializa(NUM_PARES);

                        jogada = 0;
                        peca_virada1_pos=-1;
                        peca_virada2_pos=-1;
                        pares_virados = 0;

                        textview.setTextColor(Color.rgb(0, 0, 0));
                        textview.setTextSize(16);
                        textview.setBackgroundColor(Color.rgb(255, 255, 255));
                        textview.setText("  Total Pairs Flipped: 0");
                        textview.setTypeface(Typeface.DEFAULT,Typeface.NORMAL);

                        ImgAdptr.init(NUM_PARES);
                        gridview.setAdapter(ImgAdptr);

                        return true;
                    }
                });

                popup.show(); // Mostrar menu
            }
        });


        final Button button = (Button) findViewById(R.id.button); // Iniciar novo jogo
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                pec.re_inicializa(NUM_PARES);

                jogada = 0;
                peca_virada1_pos=-1;
                peca_virada2_pos=-1;
                pares_virados = 0;

                textview.setTextColor(Color.rgb(0, 0, 0));
                textview.setTextSize(16);
                textview.setBackgroundColor(Color.rgb(255, 255, 255));
                textview.setText("  Total Pairs Flipped: 0");
                textview.setTypeface(Typeface.DEFAULT,Typeface.NORMAL);

                ImgAdptr.init(NUM_PARES);
                gridview.setAdapter(ImgAdptr);

            }
        });

        // Sempre que o jogador clicar em uma peça, serão computados os estados do jogo
        // (se as peças formarem par elas devem sair do jogo, caso contrário, suas imagens devem ser cobertas novamente)
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ImageView imageView = (ImageView) v;

                if (pec.getPecaV(position)) {
                    // Estado 1 da jogada
                    if (jogada == 0) {

                        if(pares_virados > 0) {
                            textview.setText("  Total Pairs Flipped: "+pares_virados);
                            if ((peca_virada2 == peca_virada1) && peca_virada1_pos != peca_virada2_pos) { // Não permitir que o clique em uma mesma posição conte como par
                                TJ.getIV_j1().setImageResource(pec.getNull());
                                TJ.getIV_j2().setImageResource(pec.getNull());

                                pec.setPecaValida(peca_virada1_pos);
                                pec.setPecaValida(peca_virada2_pos);

                                pec.setPos(peca_virada1_pos, pec.getNull());
                                pec.setPos(peca_virada2_pos, pec.getNull());

                                peca_virada1_pos = -1;
                                peca_virada2_pos = -1;

                            } else {
                                imageView.setImageResource(pec.getCover());
                                TJ.getIV_j1().setImageResource(pec.getCover());
                                TJ.getIV_j2().setImageResource(pec.getCover());

                            }
                        }

                            imageView.setImageResource(pec.getPeca(position));
                            peca_virada1 = pec.getPeca(position);
                            peca_virada1_pos = position;
                           TJ.setIV_j1(imageView);
                            jogada = 1;

                    }
                    // Estado 2 da jogada
                    else if (jogada == 1) {
                        if (pec.getPecaV(position)) {
                            if (position != peca_virada1_pos) { // Para não permitir que cliques simultâneos em uma mesma posição contem como cartas viradas
                                pares_virados++;
                                peca_virada2 = pec.getPeca(position);
                                peca_virada2_pos = position;
                                TJ.setIV_j2(imageView);
                                imageView.setImageResource(pec.getPeca(position));
                                jogada = 0; // O jogo só retorna ao estado 1 (verificação) se a posição selecionada for diferente da atual



                                if(pec.getPecasDisp() <= 2){ // Se só restam duas peças, isso significa que todos os pares já foram encontrados
                                    pec.setPecaValida(peca_virada1_pos);
                                    pec.setPecaValida(peca_virada2_pos);

                                    textview.setTextSize(22);
                                    textview.setText("  YOU WIN!");
                                    textview.setBackgroundColor(Color.rgb(255, 255, 0));

                                    textview.setTextColor(Color.rgb(255, 0, 255));
                                    textview.setTypeface(Typeface.DEFAULT,Typeface.BOLD);

                                }
                            }
                        }


                    } 


                }
            }

        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
