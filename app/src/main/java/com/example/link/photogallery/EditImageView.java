package com.example.link.photogallery;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Link on 03-May-16.
 */
public class EditImageView extends AppCompatActivity {

    File imageFile;
    TouchImageView imageView;
    Toolbar toolbar;
    Bitmap bitmap;
    Bitmap bitmap_none;
    Bitmap bitmap_tuned;

    int brightness;
    int contrast;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_image_view);

        initMenu();

        initBitmap();
    }

    private void initMenu() {
        toolbar = (Toolbar) findViewById(R.id.menu_editImage_top);

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initBitmap() {
        brightness = 0;
        contrast = 0;

        Intent intent = getIntent();
        imageFile = new File(intent.getStringExtra(getResources().getString(R.string.intent_image_path)));

        imageView = (TouchImageView) findViewById(R.id.iw_editImage);

        bitmap = ImageDecoder.decodeSampledBitmapFromPath(imageFile.getPath(), 500, 500);
        bitmap_none = bitmap.copy(bitmap.getConfig(), true);
        bitmap_tuned = bitmap.copy(bitmap.getConfig(), true);

        imageView.setImageBitmap(bitmap);
    }

    public void rotateLeft(View view) {
        bitmap_none = BitmapEffects.rotateLeft(bitmap_none);
        bitmap_tuned = BitmapEffects.rotateLeft(bitmap_tuned);

        bitmap = BitmapEffects.rotateLeft(bitmap);
        imageView.setImageBitmap(bitmap_tuned);

        //Toast.makeText(this, R.string.rotate_left, Toast.LENGTH_SHORT).show();
    }

    public void rotateRight(View view) {
        bitmap_none = BitmapEffects.rotateRight(bitmap_none);
        bitmap_tuned = BitmapEffects.rotateRight(bitmap_tuned);

        bitmap = BitmapEffects.rotateRight(bitmap);
        imageView.setImageBitmap(bitmap_tuned);

    }

    public void flipHorizontally(View view) {
        bitmap_none = BitmapEffects.flipHorizontally(bitmap_none);
        bitmap_tuned = BitmapEffects.flipHorizontally(bitmap_tuned);

        bitmap = BitmapEffects.flipHorizontally(bitmap);
        imageView.setImageBitmap(bitmap_tuned);
    }

    public void flipVertically(View view) {
        bitmap_none = BitmapEffects.flipVertically(bitmap_none);
        bitmap = BitmapEffects.flipVertically(bitmap);
        bitmap_tuned = BitmapEffects.flipVertically(bitmap_tuned);

        imageView.setImageBitmap(bitmap_tuned);
    }

    public void noEffect(View view) {
        bitmap = bitmap_none.copy(bitmap_none.getConfig(), true);
        bitmap_tuned = bitmap_none.copy(bitmap_none.getConfig(), true);
        bitmap_tuned = BitmapEffects.tune(bitmap_tuned, brightness, contrast / 100.f);
        imageView.setImageBitmap(bitmap_tuned);
    }

    public void createEffectBW(View view) {
        bitmap = BitmapEffects.effectBW(bitmap);
        bitmap_tuned = BitmapEffects.effectBW(bitmap_tuned);
        imageView.setImageBitmap(bitmap_tuned);
    }

    public void createEffectSepia(View view) {
        bitmap = BitmapEffects.effectSepia(bitmap);
        bitmap_tuned = BitmapEffects.effectSepia(bitmap_tuned);
        imageView.setImageBitmap(bitmap_tuned);
    }

    public void createEffectInvert(View view) {
        bitmap = BitmapEffects.effectInvert(bitmap);
        bitmap_tuned = BitmapEffects.effectInvert(bitmap_tuned);
        imageView.setImageBitmap(bitmap_tuned);
    }

    public void createEffectPolaroid(View view) {
        bitmap = BitmapEffects.effectPolaroid(bitmap);
        bitmap_tuned = BitmapEffects.effectPolaroid(bitmap_tuned);
        imageView.setImageBitmap(bitmap_tuned);
    }

    public void showEffects(View view) {
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        ViewGroup container = (ViewGroup) inflater.inflate(R.layout.effects_options, null);

        final PopupWindow popupWindow = new PopupWindow(container);
        popupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setElevation(6.f);
        popupWindow.setFocusable(true);
        popupWindow.setAnimationStyle(android.R.style.Animation_Dialog);

        Button btn_effect_none = (Button) container.findViewById(R.id.btn_effect_none);
        btn_effect_none.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                noEffect(v);
                popupWindow.dismiss();
            }
        });

        Button btn_effect_bw = (Button) container.findViewById(R.id.btn_effect_bw);
        btn_effect_bw.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                createEffectBW(v);
                popupWindow.dismiss();
            }
        });

        Button btn_effect_sepia = (Button) container.findViewById(R.id.btn_effect_sepia);
        btn_effect_sepia.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                createEffectSepia(v);
                popupWindow.dismiss();
            }
        });

        Button btn_effect_invert = (Button) container.findViewById(R.id.btn_effect_invert);
        btn_effect_invert.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                createEffectInvert(v);
                popupWindow.dismiss();
            }
        });

        Button btn_effect_polaroid = (Button) container.findViewById(R.id.btn_effect_polaroid);
        btn_effect_polaroid.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                createEffectPolaroid(v);
                popupWindow.dismiss();
            }
        });

        popupWindow.showAtLocation(imageView, Gravity.BOTTOM, 0, 450);
    }

    public void showTunes(View view) {
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        ViewGroup container = (ViewGroup) inflater.inflate(R.layout.tune_options, null);

        final PopupWindow popupWindow = new PopupWindow(container);
        popupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setElevation(6.f);
        popupWindow.setFocusable(true);
        popupWindow.setAnimationStyle(android.R.style.Animation_Dialog);

        final TextView tw_brightness = (TextView) container.findViewById(R.id.tw_brightness);
        tw_brightness.setText(String.valueOf(brightness));
        SeekBar seekbarbrightness=(SeekBar)container.findViewById(R.id.sb_brightness);
        seekbarbrightness.setMax(200);
        seekbarbrightness.setProgress(brightness + 100);
        seekbarbrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onStartTrackingTouch(SeekBar arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {

                // TODO Auto-generated method stub

                brightness = progress - 100;
                tw_brightness.setText(String.valueOf(brightness));
                bitmap_tuned = BitmapEffects.tune(bitmap, brightness, contrast / 100.f);
                imageView.setImageBitmap(bitmap_tuned);
            }
        });

        final TextView tw_contrast = (TextView) container.findViewById(R.id.tw_contrast);
        tw_contrast.setText(String.valueOf(contrast));
        SeekBar seekbarcontrast=(SeekBar)container.findViewById(R.id.sb_contrast);
        seekbarcontrast.setMax(200);
        seekbarcontrast.setProgress(contrast + 100);
        seekbarcontrast.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onStartTrackingTouch(SeekBar arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {

                // TODO Auto-generated method stub

                contrast = progress - 100;
                tw_contrast.setText(String.valueOf(contrast));
                bitmap_tuned = BitmapEffects.tune(bitmap, brightness, contrast / 100.f);
                imageView.setImageBitmap(bitmap_tuned);
            }
        });

        popupWindow.showAtLocation(imageView, Gravity.BOTTOM, 0, 450);
    }

    public void showBasicEdits(View view) {
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        ViewGroup container = (ViewGroup) inflater.inflate(R.layout.basic_edit_options, null);

        final PopupWindow popupWindow = new PopupWindow(container);
        popupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setElevation(6.f);
        popupWindow.setFocusable(true);
        popupWindow.setAnimationStyle(android.R.style.Animation_Dialog);

        Button btn_rotateLeft = (Button) container.findViewById(R.id.btn_rotateLeft);
        btn_rotateLeft.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                rotateLeft(v);
                popupWindow.dismiss();
            }
        });

        Button btn_rotateRight = (Button) container.findViewById(R.id.btn_rotateRight);
        btn_rotateRight.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                rotateRight(v);
                popupWindow.dismiss();
            }
        });

        Button btn_horizontalFlip = (Button) container.findViewById(R.id.btn_horizontalFlip);
        btn_horizontalFlip.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                flipHorizontally(v);
                popupWindow.dismiss();
            }
        });

        Button btn_verticalFlip = (Button) container.findViewById(R.id.btn_verticalFlip);
        btn_verticalFlip.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                flipVertically(v);
                popupWindow.dismiss();
            }
        });

        popupWindow.showAtLocation(imageView, Gravity.BOTTOM, 0, 450);
    }

    public void saveImage() {

        DialogInterface.OnClickListener backgroundClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:

                        boolean isPng = false;

                        if (imageFile.getName().endsWith(".png")
                                || imageFile.getName().endsWith(".PNG")) {
                            isPng = true;
                        }

                        FileOutputStream fOut = null;
                        try {
                            fOut = new FileOutputStream(imageFile);
                            if (isPng)
                            {
                                bitmap_tuned.compress(Bitmap.CompressFormat.PNG, 85, fOut);
                            }
                            else
                            {
                                bitmap_tuned.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
                            }
                            fOut.flush();
                            fOut.close();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                        }

                        setResult(RESULT_OK, null);
                        finish();

                        break;

                    case DialogInterface.BUTTON_NEGATIVE:

                        break;
                }
            }
        };

        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setMessage("Do you want to save this image?").setPositiveButton("Yes", backgroundClickListener)
                .setNegativeButton("No", backgroundClickListener).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_edit_image_top, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_save) {
            saveImage();
        }
        return super.onOptionsItemSelected(item);
    }
}
