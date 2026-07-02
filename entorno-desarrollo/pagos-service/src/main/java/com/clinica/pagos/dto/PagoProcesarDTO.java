package com.clinica.pagos.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagoProcesarDTO {
    @NotBlank(message = "El método de pago es obligatorio")
    private String metodoPago;

    private String datosPago;
}