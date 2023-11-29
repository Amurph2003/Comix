import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
export default function Database() {
  const [data, setData] = useState();
  const [search, setSearch] = useState();
  const [showButton, setShowButton] = useState(false);
  const url = "http://localhost:8080/database";
  const [category, setCategory] = useState();
  const [sortOption, setSortOption] = useState();

  const toggleButton = () => {
    setShowButton(!showButton);
  };

  useEffect(() => {
    const getData = async () => {
      console.log(url + "?searchArgs=" + search + "&sortOption=" + sortOption);
      const res = await fetch(url + "?searchArgs=" + search + "&sortOption=" + sortOption).then((res) =>
        res.json()
      );
      setData(res);
    };
    getData();
  }, [sortOption]);

  const handleForm = (e) => {
    e.preventDefault();
    const form = e.currentTarget;
    const formData = new FormData(form);
    const { query, complete } = Object.fromEntries(formData);
    console.log(query);

    //if date, reformat
    const regex = /^\d{2}\.\d{2}\.\d{4}$/;
    const isValidDate = regex.test(query);
    var term
    if(isValidDate){
      console.log('Valid date: ', isValidDate);

      const parts = query.split('.');
      console.log('Valid date: ', parts);
      const newQuery = parts[2] + '/' + parts[0] + '/' + parts[1];
      term = "search%20" + newQuery;
    }
    else{
      term = "search%20" + query;
    }

    const categories = formData.getAll('category');
    const isSeriesNameChecked = categories.includes('Series Name');
    
    if(isSeriesNameChecked){
      term += "%20Series";
    }

    const isIssueNumberChecked = categories.includes('Issue Number');
    
    if(isIssueNumberChecked){
      term += "%20issue";
    }

    const isStoryTitleChecked = categories.includes('Story Title');
    
    if(isStoryTitleChecked){
      term += "%20title";
    }

    const isPublisherChecked = categories.includes('Publisher');
    
    if(isPublisherChecked){
      term += "%20publisher";
    }

    const isCreatorsChecked = categories.includes('Creators');
    
    if(isCreatorsChecked){
      term += "%20creator";
    }

    const isPublicationDateChecked = categories.includes('Publication Date');
    
    if(isPublicationDateChecked){
      term += "%20publication_date";
    }

    const searchType = formData.getAll('searchType');
    const isPartialChecked = searchType.includes('Partial');
    
    if(isPartialChecked){
      term += "%20partial";
    } 

    const isExactChecked = searchType.includes('Exact');
    
    if(isExactChecked){
      term += "%20exact";
    }

    console.log(term);
    setSearch(term);
    const getData = async () => {
      console.log(url+term);
      const res = await fetch(url + "?searchArgs=" + term + "&sortOption=" + null).then((res) =>
        res.json()
      );
      setData(res);
    };
    getData();
  }

  const handleSortByTitle = () => {
    setSortOption("title");
  };
  
  const handleSortByDate = () => {
    setSortOption("publicationDate");
  };

  function refreshPage() {
    window.location.reload();
  }

  return (
    <div>
      <div className="h-28 bg-blue-600 h-max pb-3">
        <div><Link to={"/comixhub"} className="text-4xl pl-10 pt-5 pb-8 hover:text-white">Comix</Link></div>
        <Link to={"/personal"} className="pt-3 px-7 absolute left-20 hover:text-white">Personal Collection</Link>
        {/* <button className="pt-3 px-7 absolute right-3 hover:text-white" onClick={window.location.href='/'}>Log Out</button> */}
        <form onSubmit={handleForm}>
          <div className="flex justify-center">
            <Link to="/database" onClick={refreshPage} className="relative hover:text-white">
              <h1 className="pr-2">Reset</h1>
              <span className="absolute inset-0 bg-white opacity-0 transition-opacity duration-300"></span>
            </Link>
            <input
              placeholder='Search'
              name="query"
              className="flex justify-center hover:bg-white"
            />
            <button type="submit" className="pl-2 hover:text-white">Search</button>
          </div>
          <div id="filters" visible="false" className="flex m-0 mt-5 p-2 pb-3 bg-blue-300 justify-evenly">
            <div>
              <p>Categories:</p>
              <ul className="grid grid-cols-3">
                <li><input type="checkbox" name="category" value="Series Name"/>Series Name</li>
                <li><input type="checkbox" name="category" value="Issue Number"/>Issue Number</li>
                <li><input type="checkbox" name="category" value="Story Title"/>Story Title</li>
                <li><input type="checkbox" name="category" value="Publisher"/>Publisher</li>
                <li><input type="checkbox" name="category" value="Creators"/>Creators</li>
                <li><input type="checkbox" name="category" value="Publication Date"/>Publication Date</li>
              </ul>
            </div>
            <div className="hidden">
              <p>Comic Attributes:</p>
              <ul className="grid grid-cols-3">
                <li><input type="checkbox" name="comicAttributes" value="Graded"/>Graded</li>
                <li><input type="checkbox" name="comicAttributes" value="Slabbed"/>Slabbed</li>
                <li><input type="checkbox" name="comicAttributes" value="Authenticated"/>Authenticated</li>
                <li><input type="checkbox" name="comicAttributes" value="Signed"/>Signed</li>
                <li><input type="checkbox" name="comicAttributes" value="Runs"/>Runs</li>
                <li><input type="checkbox" name="comicAttributes" value="Gaps"/>Gaps</li>
              </ul>
            </div>
            <div className="flex flex-col">
              <p>Partial or Exact Matches:</p>
              <ul className="grid grid-cols-1">
                <li><input type="radio" name="searchType" value="Partial"/>Partial</li>
                <li><input type="radio" name="searchType" value="Exact"/>Exact</li>
              </ul>
            </div>
          </div>
        </form>
      </div>
      <div className="grid grid-cols-12">
      <div className="col-span-2">
          
        </div>
      <h1 className="col-span-2 text-center">Title</h1>
          <h2 className="col-span-2 text-center">Series Name 
              <button onClick={handleSortByTitle} value="title">
                  <span style={{ content: '', marginLeft:'5px', transform: 'translateY(-50%)', border: 'solid black', borderWidth: '0 3px 3px 0', display: 'inline-block', padding: '3px', transform: 'rotate(45deg)' }}></span>
            </button>
          </h2>
          <h2 className="col-span-1 text-center">Publisher</h2>
          <h3 className="col-span-2 text-center">Creators</h3>
          <h3 className="col-span-1 text-center">Published
            <button onClick={handleSortByDate} value="date">
                  <span style={{ content: '', marginLeft:'5px', transform: 'translateY(-50%)', border: 'solid black', borderWidth: '0 3px 3px 0', display: 'inline-block', padding: '3px', transform: 'rotate(45deg)' }}></span>
            </button>
          </h3>
          <h3 className="col-span-1 text-center">Issue</h3>
          <h3 className="col-span-1 text-center">Volume</h3>
      </div>
      <Comics comics={data }/>
    </div>
    
  );
}

const Comics = ({ comics }) => {
    const [pc, setPC] = useState();
    if (!comics) return <div>Loading...</div>;

    const getPCStuff = () => {
      const getPC = async () => {
        const res = await fetch("http://localhost:8080/collection").then((res) =>
          res.json()
        );
        setPC(res);
      };
      getPC();
    }

    return (
      <div >
        {comics.map((comic, index) => (
          <div key={index} className="border border-black grid grid-cols-12 text-center divide-x">
            {/* Border for visualization purposes */}
            <div className="col-span-2 flex flex-row items-center pl-8 "><button className="hover:text-blue-500" onClick={async () => {await fetch('http://localhost:8080/collection', { method:"PUT", body: JSON.stringify(comic), headers: {'sessionKey': '', 'Content-Type': 'application/json'}}).then((res) => res.json(), getPCStuff()); }}>Add</button><div className="w-3"></div><button className="hover:text-blue-500" onClick={async () => {await fetch('http://localhost:8080/collection', { method:"DELETE", body: JSON.stringify(comic), headers: {'sessionKey': '', 'Content-type': 'application/json'}}).then((res) => res.json(), getPCStuff()); }} >Remove</button></div>
            <h1 className="col-span-2">{comic.comicBook.title}</h1>
            <h2 className="col-span-2">{comic.series.seriesNumber}</h2>
            <h2 className="col-span-1">{comic.publisher.publisherName}</h2>
            <h3 className="col-span-2">{comic.comicBook.creators.length > 0 ? comic.comicBook.creators.join(", ") : ""}</h3>
            <h3 className="col-span-1">{comic.comicBook.publicationDate[1]}.{comic.comicBook.publicationDate[2]}.{comic.comicBook.publicationDate[0]}</h3>
            <h3 className="col-span-1">{comic.comicBook.issueNumber}</h3>
            <h3 className="col-span-1">{comic.volume.volumeNumber}</h3>
          </div>
        ))}
      </div>
    );
  };