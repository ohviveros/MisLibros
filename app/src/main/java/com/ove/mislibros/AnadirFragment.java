package com.ove.mislibros;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.orm.SugarContext;
import com.ove.mislibros.entities.Libro;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AnadirFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AnadirFragment extends Fragment {

    private static final String MODO = "param1";
    private static final int CAMARA = 101;
    private static final int GALERIA = 102;
    private String mParam1;
    private int cont = 0;

    private ImageButton ibScanner;
    private ImageButton ibCamara;
    private ImageButton ibGaleria;
    private ImageView ivPortada;
    private EditText etTitulo;
    private EditText etAutor;
    private EditText etEditorial;
    private EditText etEdicion;
    private EditText etCodigo;
    private EditText etEstado;
    private EditText etPrestatario;
    private Button bntAnadir;


    public AnadirFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment AnadirFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AnadirFragment newInstance(String param1) {
        AnadirFragment fragment = new AnadirFragment();
        Bundle args = new Bundle();
        args.putString(MODO, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(MODO);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_anadir, container, false);

        ibScanner = view.findViewById(R.id.btn_scanner);
        ibCamara = view.findViewById(R.id.btn_camara);
        ibGaleria = view.findViewById(R.id.btn_galeria);
        ivPortada = view.findViewById(R.id.iv_portada);
        etTitulo = view.findViewById(R.id.nuevo_titulo);
        etAutor = view.findViewById(R.id.nuevo_autor);
        etEdicion = view.findViewById(R.id.nuevo_edicion);
        etEditorial = view.findViewById(R.id.nuevo_editorial);
        etCodigo = view.findViewById(R.id.nuevo_codigo);
        etEstado = view.findViewById(R.id.estado);
        bntAnadir = view.findViewById(R.id.btn_anadir);

        addCamaraListener();
        addGaleriaListener();
        addScannerListener();
        addBtnAnadirListener();

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case CAMARA:
                    Bitmap imagen = (Bitmap) data.getExtras().get("data");
                    ivPortada.setImageBitmap(imagen);
                    break;
                case GALERIA:
                    Uri uriImagen = data.getData();
                    ivPortada.setImageURI(uriImagen);
                    break;
            }
        }
    }

    private void addCamaraListener() {
        ibCamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMARA);
            }
        });
    }


    private void addGaleriaListener() {
        ibGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent, GALERIA);
            }
        });
    }

    private void addScannerListener() {
        ibScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            /* TODO ojo aun sin funcion */
                Toast.makeText(getActivity().getApplicationContext(), "FALTA", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addBtnAnadirListener() {
        SugarContext.init(getActivity().getApplicationContext());

        Bitmap imagen = ((BitmapDrawable) ivPortada.getDrawable()).getBitmap();
        cont++;
        String ruta = guardarImagen(getActivity().getApplicationContext(), "imagen" + cont + "_" + (new Date()),
                imagen);

        Libro libro = new Libro(etTitulo.getText().toString(), etAutor.getText().toString(),
                etEditorial.getText().toString(), etEdicion.getText().toString(), etCodigo.getText().toString(),
                ruta, etEstado.getText().toString(), null);

        libro.save();

    }

    /**
     * Método usado para guardar la imagen de la portada del libro que ha sido obtenida
     * mediante la cámara, la galeria o el escaner del código de barras.
     *
     * @param context el contexto en que se usa.
     * @param nombre  el nombre de la imagen
     * @param imagen  la imagen a guardar
     * @return la ruta donde queda guardada
     */
    public String guardarImagen(Context context, String nombre, Bitmap imagen) {
        ContextWrapper cw = new ContextWrapper(context);
        File dirImagen = cw.getDir("Imagenes", Context.MODE_PRIVATE);
        File miRuta = new File(dirImagen, nombre + ".png");

        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(miRuta);
            imagen.compress(Bitmap.CompressFormat.JPEG, 10, fileOutputStream);
            fileOutputStream.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return miRuta.getAbsolutePath();
    }
}
