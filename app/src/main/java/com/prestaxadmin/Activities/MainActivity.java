package com.prestaxadmin.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.prestaxadmin.Fragments.DialogFragmentMessage;
import com.prestaxadmin.Listeners.ListenerVolleyResponse;
import com.prestaxadmin.Networking.KEY;
import com.prestaxadmin.Networking.RESULTCODE;
import com.prestaxadmin.Networking.RequestType;
import com.prestaxadmin.Networking.VolleyManager;
import com.prestaxadmin.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends Activity implements
        OnLongClickListener,
        RadioGroup.OnCheckedChangeListener,
        ListenerVolleyResponse,
        OnClickListener{

    private static final int SIZE_THUMB = 120;
    private static final int REQUEST_CODE_CAMERA = 1;

    private int TAG_ID = 1;
    private String type_provider = "1";

    private ImageView add_picture, more_references, less_references;
    private LinearLayout layout_images;

    private String img_path;
    private LayoutInflater inflater;
    private Map<String, String> map_photos;

    private EditText edt_customer_name,
            edt_folio_number,
            edt_telephone_number,
            edt_description_product,
            edt_condition_product,
            edt_weight_product,
            edt_amount_product,
            edt_date_start,
            edt_date_to_pay,
            edt_number_reference_0,
            edt_number_reference_1,
            edt_number_reference_2,
            edt_date_to_renovation;

    private RadioButton rbtn_pay_month,
            rbtn_pay_renovation;

    private RadioGroup group;

    private TextView txv_message_renovation, txv_message_date_pay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private LinearLayout createImageView(Bitmap bitmap){
        LinearLayout item = (LinearLayout) inflater.inflate(R.layout.layout_item_circle, null, false);
        CircleImageView circle = (CircleImageView) item.findViewById(R.id.circle_view);
        circle.setImageBitmap(bitmap);
        item.setTag(TAG_ID);
        item.setOnLongClickListener(this);
        TAG_ID ++;
        return item;
    }

    public void takePicture(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri());
        startActivityForResult(intent, REQUEST_CODE_CAMERA);
    }

    public Uri setImageUri() {
        File file = new File(Environment.getExternalStorageDirectory(), new Date().getTime() + ".jpeg");
        Uri imgUri = Uri.fromFile(file);
        this.img_path = file.getAbsolutePath();
        return imgUri;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != Activity.RESULT_CANCELED){
            if(requestCode == REQUEST_CODE_CAMERA) {
                Bitmap thumbnail = ThumbnailUtils.extractThumbnail(
                        BitmapFactory.decodeFile(img_path), SIZE_THUMB, SIZE_THUMB);

                View item = createImageView(thumbnail);
                map_photos.put(item.getTag().toString(), img_path);
                layout_images.addView(item, layout_images.getChildCount() - 1);

                if (layout_images.getChildCount() > 2)
                    add_picture.setVisibility(ImageView.GONE);
            }
        }
    }

    @Override
    public boolean onLongClick(View view) {
        map_photos.remove(view.getTag().toString());
        layout_images.removeView(view);
        add_picture.setVisibility(View.VISIBLE);
        return true;
    }

    private String getDate(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        return df.format(c.getTime());
    }

    private String getDateToPay(int months){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        Date date = c.getTime();

        if(c.get(Calendar.DAY_OF_MONTH) > 30){
            date.setMonth(date.getMonth() + months);
            date.setDate(1);
        }else{
            date.setMonth(date.getMonth() + months);
        }

        return df.format(date);
    }

    public void register(View v){
        if(validateFields()){
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.confirm_send_data))
                    .setMessage(getString(R.string.confirm_send_data_message))
                    .setPositiveButton(getString(R.string.send_data), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            sendRequest(RequestType.REGISTER, dataToMap());
                        }
                    })
                    .setNegativeButton(getString(R.string.continue_edit), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {  }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setCancelable(false)
                    .show();
        }
    }

    private void initView(){

        map_photos = new HashMap<>();
        inflater = LayoutInflater.from(this);

        layout_images = (LinearLayout) findViewById(R.id.k_layout_new_ad_upload_images);
        add_picture = (ImageView) findViewById(R.id.k_layout_new_add_image);
        more_references = (ImageView) findViewById(R.id.img_more_references);
        less_references = (ImageView) findViewById(R.id.img_less_references);
        edt_customer_name = (EditText) findViewById(R.id.edt_customer_name);
        edt_folio_number = (EditText) findViewById(R.id.edt_folio_number);
        edt_telephone_number = (EditText) findViewById(R.id.edt_telephone_number);
        edt_description_product = (EditText) findViewById(R.id.edt_description_product);
        edt_condition_product = (EditText) findViewById(R.id.edt_conditions_product);
        edt_weight_product = (EditText) findViewById(R.id.edt_weight_product);
        edt_amount_product = (EditText) findViewById(R.id.edt_amount_product);
        edt_date_start = (EditText) findViewById(R.id.edt_date_start);
        edt_date_to_pay = (EditText) findViewById(R.id.edt_date_to_pay);
        edt_number_reference_0 = (EditText) findViewById(R.id.edt_number_reference_0);
        edt_number_reference_1 = (EditText) findViewById(R.id.edt_number_reference_1);
        edt_number_reference_2 = (EditText) findViewById(R.id.edt_number_reference_2);
        edt_date_to_renovation = (EditText) findViewById(R.id.edt_date_to_renovation);

        txv_message_date_pay = (TextView) findViewById(R.id.txv_message_date_pay);

        rbtn_pay_month = (RadioButton) findViewById(R.id.rbtn_pay_month);
        rbtn_pay_renovation = (RadioButton) findViewById(R.id.rbtn_pay_renovation);

        group = (RadioGroup) findViewById(R.id.group);

        txv_message_renovation = (TextView) findViewById(R.id.txv_message_to_renovation);

        group.setOnCheckedChangeListener(this);
        more_references.setOnClickListener(this);
        less_references.setOnClickListener(this);

        edt_date_start.setText(getDate());
        edt_date_to_pay.setText(getDateToPay(1));

    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if(rbtn_pay_month.isChecked()){
                txv_message_renovation.setVisibility(View.GONE);
                edt_date_to_renovation.setVisibility(View.GONE);

                edt_date_to_pay.setText(getDateToPay(1));
                txv_message_date_pay.setText(getString(R.string.message_date_pay));
                type_provider = "1";

        } else if(rbtn_pay_renovation.isChecked()) {
            txv_message_renovation.setVisibility(View.VISIBLE);
            edt_date_to_renovation.setVisibility(View.VISIBLE);

            edt_date_to_pay.setText(getDateToPay(1));
            edt_date_to_renovation.setText(getDateToPay(2));
            txv_message_date_pay.setText(getString(R.string.message_date_to_pay));
            type_provider = "2";
        }
    }

    @Override
    public void onBackPressed(){ }

    private boolean validateFields(){
        if(edt_folio_number.getText().toString().isEmpty()){
            edt_folio_number.setError(getString(R.string.empty_field));
            edt_folio_number.requestFocus();
            return false;
        }else if(edt_customer_name.getText().toString().isEmpty()){
            edt_customer_name.setError(getString(R.string.empty_field));
            edt_customer_name.requestFocus();
            return false;
        }else if(edt_telephone_number.getText().toString().isEmpty()){
            edt_telephone_number.setError(getString(R.string.empty_field));
            edt_telephone_number.requestFocus();
            return false;
        }else if(edt_description_product.getText().toString().isEmpty()) {
            edt_description_product.setError(getString(R.string.empty_field));
            edt_description_product.requestFocus();
            return false;
        }else if(edt_condition_product.getText().toString().isEmpty()){
            edt_condition_product.setError(getString(R.string.empty_field));
            edt_condition_product.requestFocus();
            return false;
        }else if(edt_weight_product.getText().toString().isEmpty()){
            edt_weight_product.setError(getString(R.string.empty_field));
            edt_weight_product.requestFocus();
            return false;
        }else if(edt_amount_product.getText().toString().isEmpty()){
            edt_amount_product.setError(getString(R.string.empty_field));
            edt_amount_product.requestFocus();
            return false;
        }else if(edt_date_to_pay.getText().toString().isEmpty()){
            edt_date_to_pay.setError(getString(R.string.empty_field));
            edt_date_to_pay.requestFocus();
            return false;
        }else if(map_photos.isEmpty()){
            Toast.makeText(this, getString(R.string.take_a_picture), Toast.LENGTH_LONG).show();
            return false;
        }else if(edt_number_reference_0.getText().toString().isEmpty()){
            edt_number_reference_0.setError(getString(R.string.empty_field));
            edt_number_reference_0.requestFocus();
            return false;
        }else if(edt_number_reference_1.getText().toString().isEmpty() & edt_number_reference_1.getVisibility() == View.VISIBLE){
            edt_number_reference_1.setError(getString(R.string.empty_field));
            edt_number_reference_1.requestFocus();
            return false;
        }else if(edt_number_reference_2.getText().toString().isEmpty() & edt_number_reference_2.getVisibility() == View.VISIBLE){
            edt_number_reference_2.setError(getString(R.string.empty_field));
            edt_number_reference_2.requestFocus();
            return false;
        }else{
            return true;
        }
    }

    private HashMap<String, String> dataToMap(){

        HashMap<String, String> data = new HashMap<>();
        data.put(KEY.USER,              getIntent().getExtras().getString(KEY.USER));
        data.put(KEY.NAME,              edt_customer_name.getText().toString());
        data.put(KEY.FOLIO,             edt_folio_number.getText().toString());
        data.put(KEY.TELEPHONE_NUMBER,  edt_telephone_number.getText().toString());
        data.put(KEY.DESCRIPTION,       edt_description_product.getText().toString());
        data.put(KEY.CONDITIONS,        edt_condition_product.getText().toString());
        data.put(KEY.WEIGHT,            edt_weight_product.getText().toString());
        data.put(KEY.AMOUNT,            edt_amount_product.getText().toString());
        data.put(KEY.DATE_END,          edt_date_to_pay.getText().toString());
        data.put(KEY.TYPE_PROVIDER,     type_provider);
        data.put(KEY.NUMBER_REFERENCE_0,edt_number_reference_0.getText().toString());

        if(!edt_number_reference_1.getText().toString().isEmpty() & edt_number_reference_1.getVisibility() == View.VISIBLE){
            data.put(KEY.NUMBER_REFERENCE_1,edt_number_reference_1.getText().toString());
        }

        if(!edt_number_reference_2.getText().toString().isEmpty() & edt_number_reference_2.getVisibility() == View.VISIBLE){
            data.put(KEY.NUMBER_REFERENCE_2,edt_number_reference_2.getText().toString());
        }

        data.put(KEY.DATE_START,        getDate());

        ArrayList<String> array = convertMapToArrayList();

        if(array.size() == 1){
            data.put(KEY.PICTURE_1, encodeToBase64(array.get(0)));
            data.put(KEY.PICTURE_2, "0");

        }else if(array.size() == 2){
            data.put(KEY.PICTURE_1, encodeToBase64(array.get(0)));
            data.put(KEY.PICTURE_2, encodeToBase64(array.get(1)));
        }

        return data;
    }

    private ArrayList<String> convertMapToArrayList(){
        ArrayList<String> photos = new ArrayList<>();
        for (Map.Entry<String, String> entry : map_photos.entrySet()){
            photos.add(entry.getValue());
        }
        return photos;
    }

    public static String encodeToBase64(String img_path){
        Bitmap bitmap = BitmapFactory.decodeFile(img_path);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 28, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    @Override
    public void onResponse(JSONObject response) {
        try {

            if (response.has(KEY.RESULT_CODE)){

                int code = response.getInt(KEY.RESULT_CODE);

                if(code == RESULTCODE.REGISTER_FOLIO_SUCCESS || code == RESULTCODE.REGISTER_FOLIO_ALREADY){
                    String message = RESULTCODE.getMessage(this, code);
                    String password = response.getString(KEY.PASSWORD);
                    DialogFragmentMessage dialog = DialogFragmentMessage.newInstanceWithCredentials(message, RESULTCODE.SUCCESS, password);
                    dialog.show(getFragmentManager(), DialogFragmentMessage.TAG);

                }else if (code == RESULTCODE.REGISTER_FOLIO_FAILED){
                    String message = RESULTCODE.getMessage(this, code);
                    DialogFragmentMessage dialog = DialogFragmentMessage.newInstance(message, RESULTCODE.FAILED);
                    dialog.show(getFragmentManager(), DialogFragmentMessage.TAG);

                }else if(code == RESULTCODE.SEARCH_FOLIO_WITHOUT_SUCCESS){
                    String message = RESULTCODE.getMessage(this, code);
                    String password = response.getString(KEY.PASSWORD);
                    String reference = response.getString(KEY.PAYMENT_REFERENCE);
                    DialogFragmentMessage dialog = DialogFragmentMessage.newInstanceWithCredentialsAndReference(message, RESULTCODE.SUCCESS, edt_folio_number.getText().toString(),password, reference);
                    dialog.show(getFragmentManager(), DialogFragmentMessage.TAG);

                }else if(code == RESULTCODE.SEARCH_FOLIO_WITHOUT_NOT_FOUND){
                    String message = RESULTCODE.getMessage(this, code);
                    DialogFragmentMessage dialog = DialogFragmentMessage.newInstance(message, RESULTCODE.FAILED);
                    dialog.show(getFragmentManager(), DialogFragmentMessage.TAG);

                }else if (code == RESULTCODE.REGISTER_PAY_REFERENCE_SUCCESS){
                    DialogFragmentMessage dialogFragmentMessage = (DialogFragmentMessage)getFragmentManager().findFragmentByTag(DialogFragmentMessage.TAG);
                    if (dialogFragmentMessage != null && dialogFragmentMessage.isVisible()){
                        dialogFragmentMessage.dismiss();
                    }
                    String message = RESULTCODE.getMessage(this, code);
                    DialogFragmentMessage dialog = DialogFragmentMessage.newInstance(message, RESULTCODE.SUCCESS);
                    dialog.show(getFragmentManager(), DialogFragmentMessage.TAG);

                }else if (code == RESULTCODE.REGISTER_PAY_REFERENCE_FAILED){
                    String message = RESULTCODE.getMessage(this, code);
                    DialogFragmentMessage dialog = DialogFragmentMessage.newInstance(message, RESULTCODE.FAILED);
                    dialog.show(getFragmentManager(), DialogFragmentMessage.TAG);

                }

            }else if(response.has(KEY.ERROR)){
                DialogFragmentMessage dialog = DialogFragmentMessage.newInstance(response.getString(KEY.ERROR), RESULTCODE.FAILED);
                dialog.show(getFragmentManager(), DialogFragmentMessage.TAG);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendRequest(RequestType requestType, Map<String, String> params) {
        VolleyManager.getInstance().setActivity(this);
        VolleyManager.getInstance().setListener(this);
        VolleyManager.getInstance().sendRequest(requestType, params);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_more_references:
                if (edt_number_reference_1.getVisibility() == View.VISIBLE) {
                    edt_number_reference_2.setVisibility(View.VISIBLE);

                    more_references.setVisibility(View.GONE);
                    less_references.setVisibility(View.VISIBLE);

                } else if (edt_number_reference_1.getVisibility() == View.GONE) {
                    edt_number_reference_1.setVisibility(View.VISIBLE);

                    more_references.setVisibility(View.VISIBLE);
                    less_references.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.img_less_references:
                if(edt_number_reference_2.getVisibility() == View.VISIBLE){
                    edt_number_reference_2.setVisibility(View.GONE);

                    more_references.setVisibility(View.VISIBLE);
                    less_references.setVisibility(View.VISIBLE);

                } else if(edt_number_reference_2.getVisibility() == View.GONE){
                    edt_number_reference_1.setVisibility(View.GONE);

                    more_references.setVisibility(View.VISIBLE);
                    less_references.setVisibility(View.GONE);
                }
                break;
        }
    }

    public void search(View view){
        if(!edt_folio_number.getText().toString().isEmpty()){
            HashMap<String, String> data = new HashMap<>();
            data.put(KEY.FOLIO, edt_folio_number.getText().toString());
            sendRequest(RequestType.SEARCH, data);
        }else{
            edt_folio_number.setError(getString(R.string.empty_field));
            edt_folio_number.requestFocus();
        }
    }

    public void exit(View view){
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.quit))
                .setMessage(getString(R.string.are_you_sure_to_quit))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent closeSession = new Intent(MainActivity.this, Login.class);
                        startActivity(closeSession);
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}