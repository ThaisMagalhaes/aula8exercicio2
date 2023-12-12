package com.example.aula8exercicio2

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var btnChamarApi: Button
    private lateinit var txtResultado: TextView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val service = Retrofit.Builder()
            .baseUrl("https://randomuser.me/api/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(UserService::class.java)

        btnChamarApi = findViewById(R.id.btnChamarApi)
        txtResultado = findViewById(R.id.txtResultado)
        progressBar = findViewById(R.id.progressBar)

        btnChamarApi.setOnClickListener {
            // Desative o botão e mostre o ProgressBar durante a chamada da API
            btnChamarApi.isEnabled = false
            progressBar.visibility = View.VISIBLE

            service.getUsers(5).enqueue(object : Callback<ApiResponse> {
                override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                    // Restaure o estado do botão e esconda o ProgressBar
                    btnChamarApi.isEnabled = true
                    progressBar.visibility = View.INVISIBLE

                    if (response.isSuccessful) {
                        val dadosRecebidos = response.body()?.results
                        if (!dadosRecebidos.isNullOrEmpty()) {
                            // Filtra apenas os usuários que têm nomes
                            val usuariosComNomes = dadosRecebidos.filter { it.name != null }

                            if (usuariosComNomes.isNotEmpty()) {
                                // Leva apenas os primeiros 5 usuários com nomes
                                val usuarios = usuariosComNomes.take(5).map { it.name?.fullName }
                                txtResultado.text =
                                    "Dados recebidos:\n${usuarios.joinToString("\n")}"
                            } else {
                                txtResultado.text = "Nenhum usuário com nome encontrado"
                            }
                        } else {
                            txtResultado.text = "Lista de usuários vazia"
                        }
                    } else {
                        txtResultado.text = "Erro na chamada da API: ${response.message()}"
                    }
                }

                override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                    // Restaure o estado do botão e esconda o ProgressBar
                    btnChamarApi.isEnabled = true
                    progressBar.visibility = View.INVISIBLE

                    txtResultado.text = "Erro na chamada da API"
                }
            })
        }
    }
}