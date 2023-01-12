import React, { useEffect, useState, } from 'react'
import { useLocation, } from 'react-router-dom';
import { Row } from 'reactstrap';
import Console from '../componentes/Console';
import '../estilos/paginas/ConsoleLogs.scss'
export default function ConsoleLogs() {
  const location = useLocation();
  // const [host,setHost] = useState("");
  let formDataEx = location.state === null || location.state === undefined ? { masterHosts: [{ host: "localhost" }], workerHosts: [{ host: "localhost" }] } : location.state;
  const [consolaMasters, setConsolaMaster] = useState(["~"]);
  const [consolaWorkers, setConsolaWorkers] = useState(["~"]);

  const queryConfigName = new URLSearchParams(location.search).get("configname");
  const queryMasters = (new URLSearchParams(location.search).get("masters")) !== undefined?[] :new URLSearchParams(location.search).get("masters").split(",");
  const queryWorkers = (new URLSearchParams(location.search).get("workers")) !== undefined ? [] : new URLSearchParams(location.search).get("workers").split(",");

  if ((queryConfigName !== null || queryConfigName !== "") && queryMasters.length > 0 && queryWorkers.length > 0) {
    let hostsMaster = [{}];
    queryMasters.forEach(element => {
      let add = { host: element + "" }
      hostsMaster = [...add]
    })
    formDataEx = { name: queryConfigName, masterHosts: hostsMaster, workerHosts: [{ host: "localhost" }] };
  }


  useEffect(() => {

    if (formDataEx.name !== undefined) {
      formDataEx.masterHosts.map((val) => {

        const getConsola = async (name, host) => {
          let finalUrl =  process.env.REACT_APP_BACKEND_BASE_URI + "/api/v1/kubernetes/" + name + "/" + host + "/shell";
          const response = await fetch(finalUrl);
          const logs = [];
          const data = await response.json();
          console.log(data);
          setConsolaMaster(data.resultOutPut.toString().split('\n'))

        };
        getConsola(formDataEx.name, val.host);

      })
      formDataEx.workerHosts.map((val) => {
        const getConsola = async (name, host) => {
          let finalUrl =  process.env.REACT_APP_BACKEND_BASE_URI + "/api/v1/kubernetes/" + name + "/" + host + "/shell";
          const response = await fetch(finalUrl);
          const logs = [];
          const data = await response.json();
          console.log(data);
          setConsolaWorkers(data.resultOutPut.toString().split('\n'))
        };
        getConsola(formDataEx.name, val.host);

      })

    }
  }, [])
  // console.log(location)

  return (
    <div>
      <Row className='consoleLogs' >
        {formDataEx.masterHosts.map((val) => <Console name={"Control Plane (master): " + val.host} consolaHost={consolaMasters} />)}
      </Row>
      <Row className='consoleLogs'>
        {formDataEx.workerHosts.map((val) => <Console name={"Worker: " + val.host} consolaHost={consolaWorkers} />)}
      </Row>
    </div>

  )
}
