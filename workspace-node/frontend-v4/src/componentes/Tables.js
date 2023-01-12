import { React } from 'react';
import { Card, CardBody, CardHeader, Table,Button } from 'reactstrap';
import '../estilos/componentes/Table.scss'
import '../estilos/componentes/Card.scss'
import * as IconosFi from "react-icons/fi"
import { IconContext } from 'react-icons'
import { NavLink,useNavigate } from "react-router-dom"
// import {Button } from './Button'


export const Tables = ({ index,header , configs, scopeName,enlace} ) => {
  // let navigate = useNavigate();
  // const cambioVistaForm = (form) =>{
  //   navigate(form)
  // }

  
  return (
    <IconContext.Provider value={{color: 'gray'}}>
        <Card>
          <CardHeader >{header}</CardHeader>
          <CardBody>
            <Table index={index}>
              <thead>
                <tr>
                  <th className={scopeName !== ""? 'thLeft':'hide' } >{scopeName}</th>
                  <th className='thLeft' >Name</th>
                  <th className='thRight'><Button className='btn .btn-empty' onClick={
                    // event => cambioVistaForm(header === "Kubernetes"?'/kubernetes/formulario': (header + "").toLowerCase,event)
                    () =>{} // line for Nothing  to do!
                    }>+ create</Button></th>
                </tr>
              </thead>
              <tbody>
                {configs.map((item,ind) =>
                  <tr key={ind}>
                    <td className={scopeName !== ""? 'thLeft':'hide' }>{scopeName}</td>
                    <td className='thLeft'><NavLink to={enlace === undefined || enlace ==="" ? "#": enlace } className="elementK8s" >{item.name}</NavLink></td>
                    <td className='thRight'><IconosFi.FiSettings/></td>
                  </tr>
                )}
              </tbody>
            </Table>
          </CardBody>
        </Card>
        </IconContext.Provider>
  );
}