import wallpaper from './pexels-photo-7809123.png';
import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
export default function ComixHub(){ 
  return (
    <div>
      <img src={wallpaper} className="z-0 w-full h-full "/>
    <div className='absolute top-0 w-full h-full bg-slate-900/40 pt-20'>
      <h1 className="text-5xl text-center text-white">Comix Hub</h1>
      <div className="flex justify-around p-20">
        <Link to={"/database"} className="place-self-center p-40 bg-orange-500/60 text-center text-xl h-96 text-white">Database</Link>
        <Link to={"/personal"} className="place-self-center p-40 bg-green-500/60 text-center text-xl h-96 text-white">Personal Collection</Link>
        <Link to={"/search"} className="place-self-center p-40 bg-purple-500/60 text-center text-xl h-96 text-white">Statistics</Link>
      </div>
    </div>
    </div>
  );
};