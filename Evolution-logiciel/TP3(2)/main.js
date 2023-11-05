class Point
{
    constructor(x, y)
    {
        this.x = x;
        this.y = y;
    }

    toString()
    {
        return "Point";
    }
}

class Point3D extends Point
{
    constructor(x, y, z)
    {
        super(x, y);
        this.z = z;
    }

    toString()
    {
        return "Point 3D"
    }
}

function toString()
{
    return "hello";
}

function foo()
{
    let p = new Point();
    console.log(p.toString());
}

foo()