
module.exports = function(app,fs)
{
     app.get('/NFT-node-service/index',function(req,res){
        res.render('index.html')
     });

    app.get('/NFT-node-service', function (req, res) {
        res.send('Hello /');
      });
      
      app.get('/NFT-node-service/world.html', function (req, res) {
        res.send('Hello World');
      });
   
     
}
