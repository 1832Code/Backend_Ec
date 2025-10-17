package eco.market.service;

import eco.market.dto.ProductoResponse;
import eco.market.entity.Categoria;
import eco.market.entity.Producto;
import eco.market.repository.CategoriaRepository;
import eco.market.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductoService {
    
    @Autowired
    private ProductoRepository productoRepository;
    
    @Autowired
    private CategoriaRepository categoriaRepository;
    
    public List<ProductoResponse> obtenerTodosLosProductos() {
        return productoRepository.findActiveProductsOrderByDate()
                .stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }
    
    public List<ProductoResponse> obtenerProductosPorCategoria(Integer categoriaId) {
        return productoRepository.findByCategoria_CategoriaIdAndEstaActivoTrueAndEstaVerificadoTrue(categoriaId)
                .stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }
    
    public ProductoResponse obtenerProductoPorId(Integer id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        
        if (!producto.getEstaActivo() || !producto.getEstaVerificado()) {
            throw new RuntimeException("Producto no disponible");
        }
        
        return convertirAResponse(producto);
    }
    
    public List<Categoria> obtenerCategorias() {
        return categoriaRepository.findByEstaActivaTrue();
    }
    
    private ProductoResponse convertirAResponse(Producto producto) {
        ProductoResponse response = new ProductoResponse();
        response.setProductoId(producto.getProductoId());
        response.setNombreProducto(producto.getNombreProducto());
        response.setDescripcion(producto.getDescripcion());
        response.setPrecio(producto.getPrecio());
        response.setPrecioOriginal(producto.getPrecioOriginal());
        response.setStock(producto.getStock());
        response.setImagenPrincipal(producto.getImagenPrincipal());
        response.setEsOrganico(producto.getEsOrganico());
        response.setEsVegano(producto.getEsVegano());
        response.setPeso(producto.getPeso());
        response.setUnidadMedida(producto.getUnidadMedida());
        response.setCalificacionPromedio(producto.getCalificacionPromedio());
        response.setTotalCalificaciones(producto.getTotalCalificaciones());
        response.setNombreCategoria(producto.getCategoria().getNombreCategoria());
        response.setNombreVendedor(producto.getVendedor().getNombre() + " " + producto.getVendedor().getApellido());
        response.setFechaCreacion(producto.getFechaCreacion());
        return response;
    }
}