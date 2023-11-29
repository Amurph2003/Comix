import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import wallpaper from './wallpaper.png';
export default function Home() {
  const [data, setData] = useState();
  const { data: userData, error: userError, handleLogin } = useLogin();

  const handleForm = (e) => {
    e.preventDefault();
    const form = e.currentTarget;
    const formData = new FormData(form);
    const { user, password } = Object.fromEntries(formData);
    handleLogin({ username: user, password: password });
    window.location.href='/comixhub';
  };
  
  return (
    <div>
      <img src={wallpaper} className="z-0 w-full h-full"/>
    <div className="flex w-full items-center flex-col absolute top-60" >
      <div className="z-1 mt-2 max-w-md flex items-center">
        <form onSubmit={handleForm}>
          <div className="border border-black flex flex-col p-6 space-y-2 bg-slate-200">
            <label>
              <input
                name="user"
                type="text"
                placeholder="User name"
                className="p-2"
              />
            </label>
            <label>
              <input name="password" placeholder="Password" type="text" className="p-2" />
            </label>
            <button type="submit" className="bg-blue-400 text-white p-2 mt-2" > 
              Submit
            </button>
            <Link to={"/database"} className="bg-blue-400 text-white p-2 mt-2 text-center">Guest</Link>
          </div>
        </form>
      </div>
      </div>
    </div>
  );
}

const useLogin = () => {
  const [data, setData] = useState();
  const [error, setError] = useState();
  const handleLogin = async ({ username, password }) => {
    console.log(username);
    try {
      const data = await fetch("http://localhost:8080/login" + "?username=" + username + "&password=" + password, {
        method: "POST"
      }).then((res) => res.json());

      setData(data);
      // localStorage.setItem("token", JSON.stringify(data));
      console.log(data);
    } catch (e) {
      console.log(e);
      setError(e.data);
    }
  };

  return { data, handleLogin, error };
};