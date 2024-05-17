package org.example.controller;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import java.awt.Desktop;
import java.net.URI;

@Component
public class Browser implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // Endpoint que você deseja abrir no navegador
        String endpoint = "http://localhost:8080/swagger-ui/index.html#/";

        // Abre o navegador padrão do sistema com o endpoint especificado
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            Desktop.getDesktop().browse(new URI(endpoint));
        } else {
            // Caso não seja possível abrir o navegador, você pode apenas imprimir a URL
            System.out.println("Por favor, acesse manualmente o seguinte link em seu navegador: " + endpoint);
        }
    }
}