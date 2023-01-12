import React from 'react'
import logo from '../estilos/images/logoAutokeitsName.png'

import '../estilos/paginas/HomePage.scss'
function HomePage() {
    console.log("Homepage")
    return (
        <div className='home' >
           <img src={logo} width="400" height="400" class="center"/>
        </div>
    );
}
export default HomePage