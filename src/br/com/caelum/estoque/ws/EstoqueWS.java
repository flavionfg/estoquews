package br.com.caelum.estoque.ws;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;

import br.com.caelum.estoque.exception.AutorizacaoException;
import br.com.caelum.estoque.modelo.item.Filtro;
import br.com.caelum.estoque.modelo.item.Filtros;
import br.com.caelum.estoque.modelo.item.Item;
import br.com.caelum.estoque.modelo.item.ItemDao;
import br.com.caelum.estoque.modelo.item.ItemValidador;
import br.com.caelum.estoque.modelo.item.ItemValidadorException;
import br.com.caelum.estoque.modelo.item.ListaItens;
import br.com.caelum.estoque.modelo.usuario.TokenDao;
import br.com.caelum.estoque.modelo.usuario.TokenUsuario;

@WebService
@SOAPBinding(style=Style.DOCUMENT, use=Use.LITERAL,parameterStyle=ParameterStyle.WRAPPED)
public class EstoqueWS {
	
	private ItemDao dao = new ItemDao();

    @WebMethod(operationName = "todosOsItens") 
    @WebResult(name = "itens")
    public ListaItens getItens(@WebParam(name="filtros") Filtros filtros) {
        System.out.println("Chamando todosItens()");
        List<Filtro> lista = filtros.getLista();
        List<Item> itensResultado = dao.todosItens(lista);
        return new ListaItens(itensResultado);
    }
	
    @WebMethod(action="CadastrarItem", operationName="CadastrarItem") 
    @WebResult(name="item")
    public Item cadstrarItem(
    		@WebParam(name="tokenUsuario", header=true) TokenUsuario token,
    		@WebParam(name="item") Item item) throws ItemValidadorException, AutorizacaoException {
        if(!new TokenDao().ehValido(token)) {
            throw new AutorizacaoException("Autorizacao falhou");
        }

        System.out.println("Cadastrando " + item + ", " + token);
        
        //novo
        new ItemValidador(item).validate();

        this.dao.cadastrar(item);
        
        return item;
    }
}
