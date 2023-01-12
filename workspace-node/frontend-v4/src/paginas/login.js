import { React, useEffect , useState} from 'react';
import { LoginUI } from '../componentes/LoginUI';


export const Login = () => {
    const [user, setUser] = useState({}); // {} valor por defecto para que se vaya el error de undefined
    
    useEffect( // no es una funcion asincrono por eso se crea dentro de él un funcion async 
        () =>{
            const getUser = async () => {
                // puede haber un error en CORS (Cross Origin Requests) 
                // mirar en la consola web [f12] y "Console"
                // es porque el dominio localhost:puerto NO estén en el mismo dominio que el api
                // Solucion: https://www.youtube.com/watch?v=jFrbwqQJm5E minuto 8:31
                const response = fetch('https://localhost:puerto/api/login');
                const data = await response.json();
                //console.log(data);
                setUser(data);


            };
            getUser();
        }, [] // para que llame solo unavez si no se ha usado ni una vez
    );
  return (
    <div className="Login">
      <h1>AutoKeits</h1>
      {/* <h3>Login '{user.username}' </h3> */}
      <LoginUI/>
    </div>
  );
}

// function Login() {
//   return (
//     <div className="App">
//       <h1>AutoKeits</h1>
//     </div>
//   );
// }
// export default App;

// Es lo mismo