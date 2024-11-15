// Replace "YOUR_PUBLISHABLE_KEY" with your Stripe Publishable Key
const stripe = Stripe("YOUR_PUBLISHABLE_KEY");

document.addEventListener("DOMContentLoaded", () => {
  const elements = stripe.elements();
  const cardElement = elements.create("card");
  cardElement.mount("#card-element");

  document.getElementById("submit-button").addEventListener("click", async () => {
    const amount = document.getElementById("amount").value;

    if (!amount || amount <= 0) {
      alert("Please enter a valid amount.");
      return;
    }

    try {
      // Step 1: Call the backend to create a PaymentIntent
      const response = await fetch("http://localhost:8080/api/payment/create-payment-intent", {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify({ amount: amount })
      });
      
      const data = await response.json();

      // Step 2: Retrieve client secret and confirm the payment
      const { clientSecret } = data;
      const { error } = await stripe.confirmCardPayment(clientSecret, {
        payment_method: {
          card: cardElement,
        }
      });

      // Display payment result
      const messageDiv = document.getElementById("payment-message");
      if (error) {
        messageDiv.textContent = `Payment failed: ${error.message}`;
      } else {
        messageDiv.textContent = "Payment successful!";
      }
    } catch (error) {
      console.error("Error:", error);
      alert("Failed to process payment. Please try again.");
    }
  });
});
