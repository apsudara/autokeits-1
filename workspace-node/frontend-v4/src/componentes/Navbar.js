import React, { useState } from 'react'
import * as IconosFa from "react-icons/fa"
import * as IconosAi from "react-icons/ai"
import * as IconosSi from "react-icons/si"
import { NavLink } from "react-router-dom"
import "../estilos/componentes/Navbar.scss"
import { IconContext } from 'react-icons'
import * as IconoVsc from 'react-icons/vsc'
import * as IconosGi from "react-icons/gi"
import * as IconosRi from "react-icons/ri"

export const Navbar = () => {
    const [sidebar, setSidebar] = useState(false)
    const [tittle, setTittle] = useState("Home");
    const SidebarTop = [
        {
            title: 'Home',
            path: '/home',
            icon: <IconosAi.AiFillHome />,
            cName: 'nav-text'
        },
        {
            title: 'Projects',
            path: '/projects',
            icon: <IconosFa.FaProjectDiagram />,
            cName: 'nav-text'
        },
        {
            title: 'Kubernetes',
            path: '/kubernetes',
            icon: <IconosSi.SiKubernetes />,
            cName: 'nav-text'
        },
        {
            title: 'Credentials',
            path: '/credentials',
            icon: <IconosRi.RiLockPasswordLine />,
            cName: 'nav-text'
        },
        {
            title: 'Shell',
            path: '/shell',
            icon: <IconoVsc.VscTerminal />,
            cName: 'nav-text'
        }

    ];

    const SidebarBottom = [
        {
            title: 'Docs',
            path: '/documentacion',
            icon: <IconosGi.GiEvilBook />,
            cName: 'nav-text'
        },
        {
            title: 'version 0.0',
            path: '?',
            icon: <IconoVsc.VscVersions />,
            cName: 'nav-text'
        }
    ]
    const mostrarSidebar = () => setSidebar(!sidebar)
    return (
        <IconContext.Provider value={{ color: 'white' }}>
            <div className='navbar'>{tittle}</div>
            <nav className='nav-menu active nav-menu'>
                <ul className='nav-menu-items' onClick={mostrarSidebar}>
                    <li className='nav-bar-toggle'>
                        <NavLink to="#" className='menu-bars'>
                            {/* <IconosFa.FaBars/> */}
                            <IconosSi.SiKubernetes />
                        </NavLink>
                    </li>
                    {SidebarTop.map((item, index) => {
                        return (
                            <li key={index} className={item.cName} >
                                <NavLink onClick={() => setTittle(item.title)} to={item.path}>
                                    {item.icon}
                                    <span>{item.title}</span>
                                </NavLink>
                            </li>
                        )
                    })}

                </ul>
                <ul className='nav-bar-toggle-bottom'>
                    {SidebarBottom.map((item, index) => {
                        return (
                            <li key={index} className={item.cName}>
                                <NavLink onClick={() => setTittle(item.title)} to={item.path}>
                                    {item.icon}
                                    <span>{item.title}</span>
                                </NavLink>
                            </li>
                        )
                    })}
                </ul>
            </nav>
        </IconContext.Provider>
    )
}
