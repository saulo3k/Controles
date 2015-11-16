package br.com.rexapps.controles.service;

import java.time.LocalDate;
import java.util.Optional;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.rexapps.controles.domain.Pedido;
import br.com.rexapps.controles.repository.PedidoRepository;
import br.com.rexapps.controles.repository.UserRepository;
import br.com.rexapps.controles.security.SecurityUtils;


@Service
@Transactional
public class PedidoService {

    private final Logger log = LoggerFactory.getLogger(PedidoService.class);
    
    @Inject
    private PedidoRepository pedidoRepository;
    
    @Inject
    private UserRepository userRepository;
    
    public Pedido savePedido (Pedido pedido) {
        log.debug("Activating user for activation key {}", pedido);

        //Caso a data vier vazia deve colocar a data atual
        if(pedido.getDtPrevistaSeparacao() == null){
        	pedido.setDtPrevistaSeparacao(LocalDate.now());
        }
        
        if(pedido.getDtPrevistaEntrega() == null){
        	pedido.setDtPrevistaEntrega(LocalDate.now());
        }
        
        
        pedido.setDataPedido(LocalDate.now());
        pedido.setUser_pedido(userRepository.findOne(SecurityUtils.getCurrentUserId()));
        
        pedidoRepository.save(pedido);
        return null;
    }

}
