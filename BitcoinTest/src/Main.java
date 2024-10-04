import org.bitcoinj.core.*;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.script.Script;
import org.bitcoinj.store.BlockStoreException;
import org.bitcoinj.wallet.SendRequest;
import org.bitcoinj.wallet.Wallet;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws Exception {
        // Configuración de la red (MainNet o TestNet)
        NetworkParameters params = MainNetParams.get(); // Usa TestNet para pruebas

        // Crear una nueva wallet (o cargar una existente)
        Wallet wallet = Wallet.createDeterministic(params, Script.ScriptType.P2PKH);

        // Cargar una clave existente (asegúrate de tener la clave privada)
        ECKey key = new ECKey();
        wallet.importKey(key);

        // Mostrar dirección del wallet
        System.out.println("Dirección de Wallet: " + key.getPublicKeyAsHex());

        // Crear una transacción
        String toAddressString = "1A1zP1eP5QGefi2DMPTfTL5SLmv7DivfNa"; // Cambia por la dirección del destinatario
        Coin amount = Coin.parseCoin("50.000"); // Monto a enviar (0.001 BTC)


        // Crear la transacción
        SendRequest sendRequest = SendRequest.to(Address.fromString(params, toAddressString), amount);
        sendRequest.feePerKb = Coin.parseCoin("0.001"); // Tarifa de transacción

        // Enviar la transacción
        wallet.completeTx(sendRequest);
        wallet.commitTx(sendRequest.tx);
        System.out.println("Transacción enviada: " + sendRequest.tx.getTxId());
    }
}