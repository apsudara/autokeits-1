import React from 'react'
import { Card, CardBody, CardHeader,Row } from 'reactstrap';
import '../estilos/paginas/ConsoleLogs.scss'

export default function Console({name,consolaHost}) {
    console.log(consolaHost)
    return (
        <Card className='consoleLogs-col' >
            {/* <CardHeader> ConsoleLogs {formDataEx.name === undefined ? 'FORM NOT FOUND!' : formDataEx.name}</CardHeader> */}
            <CardHeader>  {name === undefined || name === null ? 'FORM NOT FOUND!' : name}</CardHeader>
            <CardBody id="growth"> ~{consolaHost.map((elem) => { return <Row>{elem}</Row> })}</CardBody>
        </Card>
    )
}
