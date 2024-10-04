const express = require('express');
const bodyParser = require('body-parser');
const bcrypt = require('bcryptjs'); // For password hashing
const app = express();
const PORT = 3000;

// Middleware to parse URL-encoded data from forms
app.use(bodyParser.urlencoded({ extended: true }));

// Serve static files (CSS, JS, etc.)
app.use(express.static('public'));

// Simulating an in-memory "database" for users and movies
let users = [];
let movies = [
        { 
            title: "Inception", 
            genre: "Sci-Fi", 
            director: "Christopher Nolan", 
            year: 2010, 
            image: "C:\Users\chait\OneDrive\Desktop\cinema-registration\public\inception.jpeg" // Add image path
        },
        { 
            title: "The Dark Knight", 
            genre: "Action", 
            director: "Christopher Nolan", 
            year: 2008, 
            image: "/images/dark_knight.jpg"
        },
        { 
            title: "Avatar", 
            genre: "Action", 
            director: "James Cameron", 
            year: 2009, 
            image: "/images/avatar.jpg"
        },
        { 
            title: "The Godfather", 
            genre: "Crime", 
            director: "Francis Ford Coppola", 
            year: 1972, 
            image: "/images/godfather.jpg"
        }
    ];
    
// Route for the root URL
app.get('/', (req, res) => {
    // Redirect the user to the registration page
    res.redirect('/register');
});

// Register route (GET) - serve registration page
app.get('/register', (req, res) => {
    res.sendFile(__dirname + '/public/register.html');
});

// Register route (POST) - handle registration and redirect to movies page
app.post('/register', (req, res) => {
    const { name, email, password, confirmPassword } = req.body;

    // Simple password validation
    if (password !== confirmPassword) {
        return res.send("Passwords do not match. Try again.");
    }

    // Check if the user already exists by email
    const existingUser = users.find(user => user.email === email);
    if (existingUser) {
        return res.send("User already exists. Please login.");
    }

    // Hash the password before saving
    const hashedPassword = bcrypt.hashSync(password, 8);

    // Create a new user
    const newUser = { name, email, password: hashedPassword };
    users.push(newUser);

    // Redirect to movie page after successful registration
    res.redirect('/movies');
});

// Login route (GET) - serve login page
app.get('/login', (req, res) => {
    res.sendFile(__dirname + '/public/login.html');
});

// Login route (POST) - handle login
app.post('/login', (req, res) => {
    const { email, password } = req.body;

    // Check if the user exists
    const user = users.find(user => user.email === email);
    if (!user) {
        return res.send({ success: false, message: "User not found. Please register." });
    }

    // Compare the hashed password
    const passwordIsValid = bcrypt.compareSync(password, user.password);
    if (!passwordIsValid) {
        return res.send({ success: false, message: "Invalid password. Please try again." });
    }

    // For demo purposes, assume login is successful
    // You might want to implement sessions or JWT here
    res.send({ success: true, message: "Login successful!" });
});

// Movies route (GET) - show the movie page with list of movies
app.get('/movies', (req, res) => {
    let movieListHTML = `
        <h2>Welcome to the Cinema!</h2>
        <p>Browse our selection of movies:</p>
        <ul class="movie-list">
    `;

    movies.forEach(movie => {
        movieListHTML += `
            <li class="movie-item">
                <img src="${movie.image}" alt="${movie.title}" class="movie-image" />
                <h3>${movie.title} (${movie.year})</h3>
                <p>Genre: ${movie.genre}</p>
                <p>Director: ${movie.director}</p>
            </li>
        `;
    });

    movieListHTML += `</ul>`;
    movieListHTML += `
        <p><a href="/book">Book a ticket</a></p>
        <p><a href="/profile">Go to Profile</a></p>
    `;

    res.send(movieListHTML);
});


// Book ticket route (GET)
app.get('/book', (req, res) => {
    res.send(`
        <h2>Book a Ticket</h2>
        <p>Select a movie and number of tickets:</p>
        <form action="/book" method="POST">
            <label for="movie">Movie:</label>
            <select id="movie" name="movie">
                ${movies.map(movie => `<option value="${movie.title}">${movie.title} (${movie.year})</option>`).join('')}
            </select>

            <label for="tickets">Number of Tickets:</label>
            <input type="number" id="tickets" name="tickets" required>

            <button type="submit">Book Ticket</button>
        </form>
    `);
});

// Book ticket route (POST)
app.post('/book', (req, res) => {
    const { movie, tickets } = req.body;
    res.send(`
        <h2>Ticket Booking Confirmed</h2>
        <p>You have successfully booked ${tickets} ticket(s) for "${movie}".</p>
        <p><a href="/movies">Back to Movie List</a></p>
    `);
});

// Profile route (GET)
app.get('/profile', (req, res) => {
    const user = users[0]; // For demo purposes, assume the first user is logged in
    if (!user) {
        return res.send("Please log in first.");
    }

    res.send(`
        <h2>Your Profile</h2>
        <p>Name: ${user.name}</p>
        <p>Email: ${user.email}</p>
        <p><a href="/update-profile">Update Profile</a></p>
    `);
});

// Profile update route (GET)
app.get('/update-profile', (req, res) => {
    const user = users[0]; // Replace with real session or JWT handling for logged-in user
    if (!user) {
        return res.send("Please log in first.");
    }

    res.send(`
        <h2>Update Your Profile</h2>
        <form action="/update-profile" method="POST">
            <label for="name">Update Name:</label>
            <input type="text" id="name" name="name" value="${user.name}" required>

            <label for="email">Update Email:</label>
            <input type="email" id="email" name="email" value="${user.email}" required>

            <label for="password">Change Password:</label>
            <input type="password" id="password" name="password">

            <label for="confirmPassword">Confirm New Password:</label>
            <input type="password" id="confirmPassword" name="confirmPassword">

            <button type="submit">Update Profile</button>
        </form>
    `);
});

// Profile update route (POST)
app.post('/update-profile', (req, res) => {
    const { name, email, password, confirmPassword } = req.body;
    const user = users[0]; // For demo purposes, assume the first user is logged in

    if (!user) {
        return res.send("User not found. Please log in.");
    }

    // Update user profile
    user.name = name;
    user.email = email;

    if (password && password === confirmPassword) {
        user.password = bcrypt.hashSync(password, 8);
    }

    res.send(`Profile updated successfully, ${user.name}. <br> <a href="/profile">Go to Profile</a>`);
});

// Start the server
app.listen(PORT, () => {
    console.log(`Server is running on http://localhost:${PORT}`);
});
