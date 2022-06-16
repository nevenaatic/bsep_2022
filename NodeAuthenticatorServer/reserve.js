const express = require('express');
const app = express();
const { JsonDB } = require('node-json-db')
const { Config } = require('node-json-db/dist/lib/JsonDBConfig')
const PORT = 8085;

app.use( express.json() )

const speakeasy = require('speakeasy')
const qrcode = require('qrcode')
var QRcode;
var retVerify;
const db = new JsonDB(new Config('PKI_Database', true, false, '/'))

//Register user    
app.post('/register', (req, res) => {
    //const { id } = req.body;
    const id = "12345"
    try{
        const path = `/user/${id}`
        const temp_secret = speakeasy.generateSecret()
        db.push(path, {id,  temp_secret})
        res.json({id, secret: temp_secret.base32})
    }
    catch(error) {
        console.log(error)
        res.status(500).json({ message: 'Error generating secret'})
    }
})

//Verify token and make secret perm
app.post('/verify', (req, res) => {
    const {token, userId} = req.body
    console.log(userId)
    console.log(token)
    try{
        const path = `/user/${userId}`
        const user = db.getData(path)

        const { base32:secret } = user.temp_secret

        const verified = speakeasy.totp.verify({
            secret,
            encoding: 'base32',
            token: token
        })
        if (verified) {
            db.push(path, {id: userId, secret: user.temp_secret})
            res.json({verified: true})
        } else {
            res.json({verified: false})
        }
    }
    catch(error){
        console.log(error)
        res.status(500).json({ message: 'Error finding user'})
    }
})


//Validate token
app.post('/validate', (req, res) => {
    const {token, userId} = req.body
    console.log(userId)
    console.log(token)
    try{
        const path = `/user/${userId}`
        const user = db.getData(path)

        const { base32:secret } = user.secret

        const tokenValidates = speakeasy.totp.verify({
            secret,
            encoding: 'base32',
            token, window: 1
        })
        if (tokenValidates) {
            db.push(path, {id: userId, secret: user.secret})
            res.json({validated: true})
        } else {
            res.json({validated: false})
        }
    }
    catch(error){
        console.log(error)
        res.status(500).json({ message: 'Error finding user'})
    }
})

app.listen(
    PORT,
    () => console.log(`it's alive on http://localhost:${PORT}`)
)

app.get('/getQRCode', (req, res) => {
    generateCode()
    setTimeout(function() {
    res.status(200).send({
        code: QRcode,
    })
    }, 200)
});

app.post('/verifyCode/', (req, res) => {

    //const { code } = req.params;
    const { code } = req.body;
    verify(code)
    console.log(code)
    res.status(200).send({
        result: retVerify
    })
});

var secret;
function generateCode(){

    secret = speakeasy.generateSecret({
        name: "PKI App"
    })

    qrcode.toDataURL(secret.otpauth_url, function(err, data){
        console.log(data)
        QRcode = data
    })
}

function verify(code) {
    console.log(code)
    console.log(secret)
    console.log(secret.ascii)
    var verified = speakeasy.totp.verify({
        secret: secret.ascii,
        encoding: 'ascii',
        token: code
    })
    console.log(verified)
    retVerify = verified;
}