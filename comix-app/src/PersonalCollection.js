import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
export default function Personal() {
  const [data, setData] = useState();
  const [search, setSearch] = useState();
  const [showButton, setShowButton] = useState(false);
  const [sortOption, setSortOption] = useState();
  const url = "http://localhost:8080/collection";


    const toggleButton = () => {
      setShowButton(!showButton);
    };
  
    useEffect(() => {
      const getData = async () => {
        const res = await fetch(url + "?searchArgs=" + search + "&sortOption=" + sortOption, {
          headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
            'sessionKey': 'asd'
          }}).then((res) =>
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
      const res = await fetch(url + "?searchArgs=" + term + "&sortOption=" + null, {
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded',
          'sessionKey': 'asd'
        }}).then((res) =>
        res.json()
      );
      if (Object.keys(res).length === 0) {
        setData({});
      } else {
        setData(res);
      }
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
        <Link to={"/comixhub"} className="text-4xl "><h1 className="pl-10 pt-5 hover:text-blue-500">Comix</h1></Link>
        <Link to={"/database"} className="pt-3 px-7 absolute left-20 hover:text-blue-500">Database Collection</Link>
        {/* <button className="pt-3 px-7 absolute right-3 hover:text-blue-500" onClick={window.location.href='/'}>Log Out</button> */}
        <form onSubmit={handleForm}>
          <div className="flex justify-center">
            <Link to="/personal" onClick={refreshPage} className="relative hover:text-blue-500">
              <h1 className="pr-2">Reset</h1>
              <span className="absolute inset-0 bg-blue-100 opacity-0 transition-opacity duration-300"></span>
            </Link>
            <input
              placeholder='Search'
              name="query"
              className="flex justify-center hover:bg-blue-200"
            />
            <button className="pl-2 hover:text-blue-500" type="submit">Search</button>
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
            <div className="">
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
              <p>Full or Partial Matches:</p>
              <ul className="grid grid-cols-1">
              <li><input type="radio" name="searchType" value="Partial"/>Partial</li>
                <li><input type="radio" name="searchType" value="Exact"/>Exact</li>
              </ul>
            </div>
          </div>
        </form>
      </div>
      <div className="grid grid-cols-12">
        <div className="col-span-2 text-center">
          {/* <button onClick={handleSortByDate} value="undo" className="bg-blue-800 text-white px-0 py-0 rounded-md mr-2">
            <span>Undo</span>
          </button>
          <button onClick={handleSortByDate} value="redo" className="bg-blue-800 text-white px-0 py-0 rounded-md">
            <span>Redo</span>
          </button> */}
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
      <Comics comics={data}/>
    </div>
  );
}



const Comics = ({ comics }) => {
    console.log(comics)
    if (!comics) return <div>Loading...</div>;
    if (typeof comics === 'object' && Object.keys(comics).length === 0) return <div>No Results</div>;
    // if (typeof comics === 'string') return <div>Loading...</div>;
    return (
      <div>
        {comics.map((comic, index) => (
          <div key={index} className="border border-black grid grid-cols-12 text-center divide-x" >
            {/* Border for visualization purposes */}
            <div className="col-span-2"><button onClick={async () => {await fetch('http://localhost:8080/collection', { method:"DELETE", body: JSON.stringify(comic), headers: {'sessionKey': '', 'Content-type': 'application/json'}}).then((res) => res.json(), window.location.reload()); }}  className="z-5 hover:text-blue-500">Remove</button></div>
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


 