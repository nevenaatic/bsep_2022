const express = require('express');
const app = express();
const PORT = 8085;
app.use( express.json() )

const speakeasy = require('speakeasy')
const qrcode = require('qrcode')
var QRcode;
var retVerify;


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