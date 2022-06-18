const express = require('express');
const cors = require('cors');
const app = express();
const { JsonDB } = require('node-json-db')
const { Config } = require('node-json-db/dist/lib/JsonDBConfig')
const PORT = 8085;

app.use( express.json() )
app.use(cors())
app.listen(
    PORT,
    () => console.log(`it's alive on http://localhost:${PORT}`)
)
const speakeasy = require('speakeasy')
const qrcode = require('qrcode')

const db = new JsonDB(new Config('PKI_Database', true, false, '/'))
const dbDislinkt = new JsonDB(new Config('Dislinkt_Database', true, false, '/'))

//Register user    
app.post('/register/:id', (req, res) => {
    const { id } = req.params;
    const path = `/user/${id}`
    try {
        db.getData(path)
        res.status(200).json({alreadyRegistered: true})
    } catch (error){
        try{
            const temp_secret = speakeasy.generateSecret({
                name: "PKI App"
            })
            db.push(path, {id,  temp_secret})
            qrcode.toDataURL(temp_secret.otpauth_url, function(err, data){
                console.log(data)
                res.json({qrCode: data})
            })
            //res.json({id, secret: temp_secret.base32})
        }
        catch(error) {
            console.log(error)
            res.status(500).json({ message: 'Error generating secret'})
        }
    }
})

app.post('/registerDislinkt/:id', (req, res) => {
    const { id } = req.params;
    const path = `/user/${id}`
    try {
        dbDislinkt.getData(path)
        res.status(200).json({alreadyRegistered: true})
    } catch (error){
        try{
            const temp_secret = speakeasy.generateSecret({
                name: "Dislinkt App"
            })
            dbDislinkt.push(path, {id,  temp_secret})
            qrcode.toDataURL(temp_secret.otpauth_url, function(err, data){
                console.log(data)
                res.json({qrCode: data})
            })
            //res.json({id, secret: temp_secret.base32})
        }
        catch(error) {
            console.log(error)
            res.status(500).json({ message: 'Error generating secret'})
        }
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
            //db.push(path, {id: userId, secret: user.temp_secret})
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

app.post('/verifyDislinkt', (req, res) => {
    const {token, userId} = req.body
    console.log(userId)
    console.log(token)
    try{
        const path = `/user/${userId}`
        const user = dbDislinkt.getData(path)

        const { base32:secret } = user.temp_secret

        const verified = speakeasy.totp.verify({
            secret,
            encoding: 'base32',
            token: token
        })
        if (verified) {
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

