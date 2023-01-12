import React from 'react'
import '../estilos/componentes/Button.scss'
import { Button as Boton } from 'reactstrap';
export const Button = ({name,icono,accion})=> {
  return (
    <Boton className={icono!=="" ? 'btn': 'btn-empty' } onClick={accion}> {icono} {name}</Boton>
  )
}

