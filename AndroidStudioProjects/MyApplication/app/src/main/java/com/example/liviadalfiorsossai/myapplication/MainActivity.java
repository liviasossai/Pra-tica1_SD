package com.example.liviadalfiorsossai.myapplication;

import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {
public int jogada = 0;
public int peca_virada1;
public int peca_virada2;
public int peca_virada1_pos=-1;
public int peca_virada2_pos=-1;
    public int pares_virados = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        ImageView anim = (ImageView) findViewById(R.id.animacao);
        anim.setBackgroundResource(R.drawable.animacao);

        AnimationDrawable animation = (AnimationDrawable) anim.getBackground();
        animation.start();


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView textview = (TextView) findViewById(R.id.textView);


        final TratadorPecas pec = new TratadorPecas(12);

        final TratadorJogada TJ = new TratadorJogada();

        final GridView gridview = (GridView) findViewById(R.id.GV);

        gridview.setAdapter(new ImageAdapter(this));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ImageView imageView = (ImageView) v;

                if (pec.getPecaV(position)) {
                    if (jogada == 0) {

                        if(pares_virados > 0) {
                            textview.setText("Total Pairs Flipped: "+pares_virados);
                            if ((peca_virada2 == peca_virada1) && peca_virada1_pos != peca_virada2_pos) {
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
                        //if(peca_virada2_pos != position) {
                            imageView.setImageResource(pec.getPeca(position));
                            peca_virada1 = pec.getPeca(position);
                            peca_virada1_pos = position;
                            TJ.setIV_j1(imageView);
                            jogada = 1;
                       // }
                    } else if (jogada == 1) {
                        if (pec.getPecaV(position)) {
                            if (position != peca_virada1_pos) {
                                pares_virados++;
                                peca_virada2 = pec.getPeca(position);
                                peca_virada2_pos = position;
                                TJ.setIV_j2(imageView);
                                imageView.setImageResource(pec.getPeca(position));
                                jogada = 0;
                            }
                        }


                    } else if (jogada == 2) {
                        jogada = 0;
                        pares_virados++;

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
