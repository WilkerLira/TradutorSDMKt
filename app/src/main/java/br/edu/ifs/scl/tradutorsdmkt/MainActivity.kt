package br.edu.ifs.scl.tradutorsdmkt

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.widget.ArrayAdapter
import br.edu.ifs.scl.tradutorsdmkt.MainActivity.codigosMensagem.RESPOSTA_TRADUCAO
import br.edu.ifs.scl.tradutorsdmkt.volley.Tradutor
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.design.snackbar

class MainActivity : AppCompatActivity() {

    object codigosMensagem {
        //Constante usada para envio de mensagens ao Handler
        val RESPOSTA_TRADUCAO = 0
    }

    //Idiomas de origem e destino. Denpende da api Oxford dicionario
    val idiomas = listOf("pt", "en")

    //Handler da thread de UI
    lateinit var tradutorHandler: TradutorHandler

    inner class TradutorHandler : Handler() {
        override fun handleMessage(msg: Message?) {
            if (msg?.what == RESPOSTA_TRADUCAO) {
                //Altera o conteudo do TextView
                traduzidoTextView.text = msg.obj.toString()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Instancia o Handler da thread da UI usado pelo tradutor
        tradutorHandler = TradutorHandler()

        //Cria e seta um Adapter com os idiomas de origem para o Spinner
        idiomaOrigemSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, idiomas)
        idiomaOrigemSpinner.setSelection(0)//pt

        //Cria e seta um Adapter com os idiomas de origem para o Spinner
        idiomaDestinoSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, idiomas)
        idiomaDestinoSpinner.setSelection(1)//en

        //Seta o Listener para o Botão
        traduzirButton.setOnClickListener {
            //Testa se o usuário digitou alguma coisa para traduzir
            if (originalEditText.text.isNotEmpty()) {
                //Instancia um tradutor para fazer a chamada ao WebService
                val tradutor: Tradutor =
                    Tradutor(this)

                //Solicita a tradução com base nos parâmetros selecionados pelo usuário
                tradutor.traduzir(
                    originalEditText.text.toString(),
                    idiomaOrigemSpinner.selectedItem.toString(),
                    idiomaDestinoSpinner.selectedItem.toString()
                )
            } else {
                //Senão, mostra uma mensagem na parte debaixo do LinearLayout
                mainLl.snackbar("É preciso digitar uma palavra para ser traduzida")
            }
        }
    }
}
