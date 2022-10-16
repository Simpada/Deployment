import {useEffect, useState} from "react";
import './App.css'

function App() {

  return (
    <div className="App">

        <h1> Welcome to Mimpada's WebStore! </h1>
        <h3> You want? </h3>
        <h3> Too bad, we only sell forks! </h3>
        <ProductList/>
        <hr/>
        <AddProduct/>
    </div>
  )
}

function ProductList() {

    const [loading, setLoading] = useState(true);
    const [products, setProduct] = useState([]);

    useEffect(()=> {
        (async () => {
            const res = await fetch("/api/products");
            setProduct(await res.json());
            setLoading(false);
        })();
    }, [])

    if (loading) {
        return <div>Loading...</div>
    }

    return (
        <>
            <br/>
            <h2>Products</h2>
            <ul className="products">
                {
                    products.map(
                        p => {
                            return (<li>
                                <hr/>
                                <h3 className="item">{p.name}</h3>
                                <p className="item">Category: {p.category.toLowerCase()}</p>
                                <p className="item">Price: {p.price} 🐶</p>
                            </li>)
                        }
                    )
                }
            </ul>
        </>
    )
}

function AddProduct() {
    const [name, setName] = useState("Product");
    const [category, setCat] = useState("FORKS");
    const [price, setPrice] = useState("0");

    async function submitProduct(e) {
        e.preventDefault();

        await fetch("/api/products", {
            method: "post",
            body: JSON.stringify({name, category, price}),
            headers: {
                "Content-type": "application/json"
            }
        })

        window.location.reload()
    }

    return (<>
        <h2>Add product</h2>
        <form onSubmit={submitProduct}>
            <label>Product name: </label>
            <input type="text" name={"name"} onChange={e => setName(e.target.value)} value={name}/>
            <br/>

            <label>Category: </label>
            <select name={"category"} onChange={e => setCat(e.target.value)} value={category}>
                <option value="A_FORK">A Fork</option>
                <option value="FORKSES">Forkses</option>
                <option value="ALSO_FORKS">Also Forks</option>
                <option value="FORKS">Forks</option>
            </select>
            <br/>

            <label>Price: </label>
            <input type="number" name={"price"} onChange={e => setPrice(e.target.value + "")} value={price}/>
            <br/>

            <button>Register</button>
        </form>
    </>)
}

export default App
