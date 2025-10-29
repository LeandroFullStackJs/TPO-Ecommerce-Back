package com.api.e_commerce.service;

import com.api.e_commerce.dto.DireccionDTO;
import com.api.e_commerce.model.Direccion;
import com.api.e_commerce.repository.DireccionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DireccionService {
    
    @Autowired
    private DireccionRepository direccionRepository;
    
    public List<DireccionDTO> obtenerTodasLasDirecciones() {
        return direccionRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
    
    public Optional<DireccionDTO> obtenerDireccionPorId(Long id) {
        return direccionRepository.findById(id)
                .map(this::convertirADTO);
    }
    
    public DireccionDTO crearDireccion(DireccionDTO direccionDTO) {
        Direccion direccion = convertirAEntidad(direccionDTO);
        Direccion direccionGuardada = direccionRepository.save(direccion);
        return convertirADTO(direccionGuardada);
    }
    
    public Optional<DireccionDTO> actualizarDireccion(Long id, DireccionDTO direccionDTO) {
        return direccionRepository.findById(id)
                .map(direccion -> {
                    direccion.setCalle(direccionDTO.getCalle());
                    direccion.setNumero(direccionDTO.getNumero());
                    direccion.setLocalidad(direccionDTO.getLocalidad());
                    direccion.setProvincia(direccionDTO.getProvincia());
                    direccion.setPais(direccionDTO.getPais());
                    return convertirADTO(direccionRepository.save(direccion));
                });
    }
    
    public boolean eliminarDireccion(Long id) {
        if (direccionRepository.existsById(id)) {
            direccionRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    private DireccionDTO convertirADTO(Direccion direccion) {
        DireccionDTO dto = new DireccionDTO();
        dto.setId(direccion.getId());
        dto.setCalle(direccion.getCalle());
        dto.setNumero(direccion.getNumero());
        dto.setLocalidad(direccion.getLocalidad());
        dto.setProvincia(direccion.getProvincia());
        dto.setPais(direccion.getPais());
        return dto;
    }
    
    private Direccion convertirAEntidad(DireccionDTO dto) {
        Direccion direccion = new Direccion(null, null, null, null, null, null);
        direccion.setCalle(dto.getCalle());
        direccion.setNumero(dto.getNumero());
        direccion.setLocalidad(dto.getLocalidad());
        direccion.setProvincia(dto.getProvincia());
        direccion.setPais(dto.getPais());
        return direccion;
    }
}