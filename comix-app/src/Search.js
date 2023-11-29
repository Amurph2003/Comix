// import { useState, useEffect } from "react";
// import { Link } from "react-router-dom";
// export default function Search(Data) {
//     const [data, setData] = useState();
//     const [search, setSearch] = useState("");
//     const [showButton, setShowButton] = useState(false);
//     const url = "http://localhost:8080/collection";

//     const toggleButton = () => {
//         setShowButton(!showButton);
//     };
    

//     return (
//         <div>
//         <div className="h-28 bg-blue-600 h-max pb-3">
//         <Link to={"/comixhub"} className="text-4xl "><h1 className="pl-10 pt-5">Comix</h1></Link>
//         <Link to={"/database"} className="pt-3 px-7 absolute left-20">Database Collection</Link>
//         <button className="pt-3 px-7 absolute right-3">Log Out</button>
//         <form onSubmit={handleForm}>
//           <div className="flex justify-center">
//             <input placeholder='Search' name="query" className="flex justify-center"/>
//             <button onClick={toggleButton} className="pl-2">Filter</button>
//           </div>
//           {showButton && <div id="filters" visible="false" className="flex m-0 mt-5 p-2 pb-3 bg-blue-300 justify-evenly">
//             <p>Categories:</p>
//             <ul className="grid grid-cols-3">
//               <li><input type="checkbox"/>Series Title</li>
//               <li><input type="checkbox"/>Issue Number</li>
//               <li><input type="checkbox"/>Story Title</li>
//               <li><input type="checkbox"/>Publisher</li>
//               <li><input type="checkbox"/>Creators</li>
//               <li><input type="checkbox"/>Publication Date</li>
//             </ul>
//             <p>Comic Attributes:</p>
//             <ul className="grid grid-cols-3">
//               <li><input type="checkbox"/>Graded</li>
//               <li><input type="checkbox"/>Slabbed</li>
//               <li><input type="checkbox"/>Authenticated</li>
//               <li><input type="checkbox"/>Signed</li>
//               <li><input type="checkbox"/>Runs</li>
//               <li><input type="checkbox"/>Gaps</li>
//             </ul>
//           </div>}
//         </form>
//       </div>
//       <div className="grid grid-cols-12">
//           <div className="col-span-2"></div>
//           <h1 className="col-span-2 text-center">Title</h1>
//           <h2 className="col-span-2 text-center">Series Name</h2>
//           <h2 className="col-span-1 text-center">Publisher</h2>
//           <h3 className="col-span-2 text-center">Creators</h3>
//           <h3 className="col-span-1 text-center">Published</h3>
//           <h3 className="col-span-1 text-center">Issue</h3>
//           <h3 className="col-span-1 text-center">Volume</h3>
//       </div>
//       <Comics comics={data}/>
//     </div>
    
//     );
// }
